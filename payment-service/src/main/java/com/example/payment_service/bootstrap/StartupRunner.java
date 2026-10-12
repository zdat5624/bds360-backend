package com.example.payment_service.bootstrap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment_service.modules.transaction.constant.TransactionStatus;
import com.example.payment_service.modules.transaction.constant.TransactionType;
import com.example.payment_service.modules.transaction.entity.Transaction;
import com.example.payment_service.modules.transaction.repository.TransactionRepository;
import com.example.payment_service.modules.transaction.util.VnPayUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final TransactionRepository transactionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        System.out.println(">>> START INIT PAYMENT DATABASE");

        if (transactionRepository.count() > 0) {
            System.out.println(">>> SKIP! ALREADY HAVE TRANSACTION DATA...");
        } else {
            initSampleTransactions();
        }

        System.out.println(">>> END INIT PAYMENT DATABASE");
    }

    @Transactional
    private void initSampleTransactions() {
        System.out.println(">>> READING USERS.CSV TO MOCK DATA...");
        List<MockUser> mockUsers = new ArrayList<>();

        // Nhờ Gradle, file users.csv cũng nằm chình ình trong /data/ của Payment
        // Service
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/users.csv").getInputStream()))) {

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < cols.length; i++)
                    cols[i] = cols[i].replaceAll("^\"|\"$", "");

                // Trích xuất ID (Cột 0), Email (Cột 6), Tên (Cột 9)
                mockUsers.add(new MockUser(Long.parseLong(cols[0]), cols[6], cols[9]));
            }
        } catch (Exception e) {
            System.err.println(">>> LỖI ĐỌC MOCK DATA TỪ CSV: " + e.getMessage());
            return;
        }

        System.out.println(">>> SEEDING TRANSACTIONS...");
        List<TransactionStatus> statuses = Arrays.asList(TransactionStatus.PENDING, TransactionStatus.SUCCESS,
                TransactionStatus.FAILED);
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();

        int totalTransactions = 500;

        for (int i = 1; i <= totalTransactions; i++) {
            Transaction transaction = new Transaction();

            // 🌟 Ép dữ liệu chuẩn xác 100% từ Identity sang Transaction
            MockUser selectedUser = mockUsers.get(random.nextInt(mockUsers.size()));
            transaction.setUserId(selectedUser.getId());
            transaction.setUserEmail(selectedUser.getEmail());
            transaction.setUserName(selectedUser.getName());

            boolean isDeposit = random.nextBoolean();
            String txnId = VnPayUtil.getRandomNumber(8) + "_" + i;

            if (isDeposit) {
                // Giao dịch nạp tiền (DEPOSIT)
                long amount = 50_000L + (long) (random.nextDouble() * 9_950_000L);
                TransactionStatus selectedStatus = statuses.get(random.nextInt(statuses.size()));

                transaction.setAmount(amount);
                transaction.setStatus(selectedStatus);
                transaction.setType(TransactionType.DEPOSIT);
                transaction.setDescription("Giao dịch nạp tiền " + selectedStatus.name());
                transaction.setPaymentLink("https://payment.example.com/txn/" + txnId);
                transaction.setTxnId(txnId);
            } else {
                // Giao dịch thanh toán (PAYMENT) - Số âm
                long amount = -(10_000L + (long) (random.nextDouble() * 490_000L));
                transaction.setAmount(amount);
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setType(TransactionType.PAYMENT);
                transaction.setDescription("Thanh toán dịch vụ thành công");
                transaction.setPaymentLink(null);
                transaction.setTxnId(null);
            }

            // Giao dịch sinh ngẫu nhiên trong vòng 6 tháng qua
            transaction.setCreatedAt(Instant.now().minusSeconds((long) (random.nextDouble() * 15552000L)));
            if (transaction.getStatus() != TransactionStatus.PENDING) {
                transaction.setUpdatedAt(transaction.getCreatedAt().plusSeconds(random.nextInt(3600)));
            }

            transactions.add(transaction);

            // Ghi xuống DB mỗi khi đủ 100 record để giải phóng RAM
            if (transactions.size() >= 100) {
                transactionRepository.saveAll(transactions);
                transactions.clear();
            }
        }

        if (!transactions.isEmpty()) {
            transactionRepository.saveAll(transactions);
        }

        System.out.println(">>> SEEDING TRANSACTIONS COMPLETED!");
    }

    // Class nội bộ dùng để hứng dữ liệu tạm trên RAM
    private static class MockUser {
        private final Long id;
        private final String email;
        private final String name;

        public MockUser(Long id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }
    }
}