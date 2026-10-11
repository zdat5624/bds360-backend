package com.example.identity_service.bootstrap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.identity_service.common.constant.Role;
import com.example.identity_service.modules.user.constant.Gender;
import com.example.identity_service.modules.user.constant.VerificationStatus;
import com.example.identity_service.modules.user.entity.User;
import com.example.identity_service.modules.user.entity.VerificationSubmission;
import com.example.identity_service.modules.user.repository.UserRepository;
import com.example.identity_service.modules.user.repository.VerificationSubmissionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final UserRepository userRepository;
    private final VerificationSubmissionRepository verificationRepo;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {

        System.out.println(">>> START INIT IDENTITY DATABASE: 3");

        System.out.println(">>> INIT TABLE 'users': 1 ADMIN, 50 USER");

        if (this.userRepository.count() > 0) {
            System.out.println(">>> SKIP! INIT TABLE 'users' : ALREADY HAVE DATA ... ");
        } else {
            initSampleUsers();
        }

        System.out.println(">>> END INIT IDENTITY DATABASE");
    }

    @Transactional
    private void initSampleUsers() {
        if (userRepository.count() > 1)
            return; // Tránh seed đè dữ liệu

        List<User> userList = new ArrayList<>();
        String password = passwordEncoder.encode("123456");
        Random random = new Random();
        Instant now = Instant.now();

        // ===== DATA NAME =====
        String[] lastNames = { "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Đặng" };
        String[] maleMiddleNames = { "Văn", "Hữu", "Đức" };
        String[] femaleMiddleNames = { "Thị", "Ngọc", "Thu" };
        String[] maleFirstNames = { "Anh", "Bình", "Cường", "Dũng", "Hải", "Hưng", "Khánh", "Minh", "Nam" };
        String[] femaleFirstNames = { "Anh", "Hà", "Hương", "Lan", "Linh", "Mai", "Ngọc", "Thảo", "Trang" };

        String[] addresses = { "Hà Nội", "Đà Nẵng", "TP. Hồ Chí Minh" };

        // ===== 1. ADMIN =====
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setName("Quản trị viên");
        admin.setPassword(password);
        admin.setRole(Role.ADMIN);
        admin.setGender(Gender.MALE);
        admin.setAvatar("https://randomuser.me/api/portraits/men/97.jpg");
        admin.setBalance(1_000_000_000L);
        admin.setPhone("0123456789");
        admin.setAddress("TP. Hồ Chí Minh");
        admin.setCreatedAt(now.minus(4 * 365L, java.time.temporal.ChronoUnit.DAYS));
        userList.add(admin);

        // ===== 2. USERS =====
        for (int i = 1; i <= 50; i++) {
            User user = new User();

            user.setEmail("user" + i + "@gmail.com");
            user.setPassword(password);
            user.setRole(Role.USER);

            // Giới tính
            Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
            user.setGender(gender);

            // ===== RANDOM NAME =====
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String middleName = (gender == Gender.MALE)
                    ? maleMiddleNames[random.nextInt(maleMiddleNames.length)]
                    : femaleMiddleNames[random.nextInt(femaleMiddleNames.length)];

            String firstName = (gender == Gender.MALE)
                    ? maleFirstNames[random.nextInt(maleFirstNames.length)]
                    : femaleFirstNames[random.nextInt(femaleFirstNames.length)];

            user.setName(lastName + " " + middleName + " " + firstName);

            // ===== AVATAR =====
            String genderApiDir = (gender == Gender.MALE) ? "men" : "women";
            int avatarIndex = random.nextInt(100); // tránh vượt range
            user.setAvatar("https://randomuser.me/api/portraits/" + genderApiDir + "/" + avatarIndex + ".jpg");

            // ===== OTHER INFO =====
            user.setBalance(500_000L * i);
            user.setPhone("09" + String.format("%08d", random.nextInt(100_000_000)));
            user.setAddress(addresses[random.nextInt(addresses.length)]);

            long randomDaysAgo = random.nextLong(1, 4 * 365 + 1);
            // Trừ thêm một lượng giây ngẫu nhiên trong ngày đó cho nó thật
            long randomSecondsInDay = random.nextLong(86400);

            Instant randomCreatedAt = now
                    .minus(randomDaysAgo, java.time.temporal.ChronoUnit.DAYS)
                    .minusSeconds(randomSecondsInDay);

            user.setCreatedAt(randomCreatedAt);
            // Cập nhật updatedAt bằng createdAt luôn cho logic
            user.setUpdatedAt(randomCreatedAt);

            userList.add(user);
        }

        userRepository.saveAll(userList);

        // ===== 3. VERIFICATION =====
        seedVerificationSubmissions(userList);
    }

    private void seedVerificationSubmissions(List<User> users) {
        List<VerificationSubmission> submissions = new ArrayList<>();
        Instant now = Instant.now();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            // Reset mặc định
            user.setIsVerified(false);

            int caseType = i % 3;

            switch (caseType) {
                case 0 -> {
                    // ===== CASE 1: Đã duyệt =====
                    submissions.add(createSub(
                            user,
                            VerificationStatus.REJECTED,
                            "Ảnh mờ",
                            now.minusSeconds(86400 * 3)));

                    submissions.add(createSub(
                            user,
                            VerificationStatus.APPROVED,
                            "Hợp lệ",
                            now.minusSeconds(86400)));

                    user.setIsVerified(true);
                }

                case 1 -> {
                    // ===== CASE 2: Bị từ chối + đang chờ =====
                    submissions.add(createSub(
                            user,
                            VerificationStatus.REJECTED,
                            "Sai định dạng",
                            now.minusSeconds(86400 * 2)));

                    submissions.add(createSub(
                            user,
                            VerificationStatus.PENDING,
                            null,
                            now.minusSeconds(3600)));

                    user.setIsVerified(false);
                }

                case 2 -> {
                    // ===== CASE 3: Mới gửi lần đầu =====
                    submissions.add(createSub(
                            user,
                            VerificationStatus.PENDING,
                            null,
                            now.minusSeconds(7200)));

                    user.setIsVerified(false);
                }
            }
        }

        // Lưu toàn bộ user (để cập nhật lại trạng thái isVerified)
        userRepository.saveAll(users);

        // Lưu toàn bộ submissions
        verificationRepo.saveAll(submissions);
    }

    // Hàm helper để tạo object submission nhanh gọn
    private VerificationSubmission createSub(User user, VerificationStatus status, String note, Instant createdAt) {
        VerificationSubmission sub = VerificationSubmission.builder()
                .user(user)
                .idCardFront(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/C%C4%83n_c%C6%B0%E1%BB%9Bc_c%C3%B4ng_d%C3%A2n_g%E1%BA%AFn_ch%C3%ADp_m%E1%BA%B7t_tr%C6%B0%E1%BB%9Bc.jpg/960px-C%C4%83n_c%C6%B0%E1%BB%9Bc_c%C3%B4ng_d%C3%A2n_g%E1%BA%AFn_ch%C3%ADp_m%E1%BA%B7t_tr%C6%B0%E1%BB%9Bc.jpg")
                .idCardBack("https://media.vov.vn/sites/default/files/styles/large/public/2021-10/Can%20cuoc.jpg")
                .status(status)
                .reviewNote(note)
                .build();

        // Vì Builder không set được @PrePersist khi seed thủ công có timeline, ta set
        // cứng createdAt
        sub.setCreatedAt(createdAt);

        if (status != VerificationStatus.PENDING) {
            sub.setReviewedAt(createdAt.plusSeconds(3600 * 2)); // Duyệt sau khi nộp 2 tiếng
            sub.setReviewedBy("admin@gmail.com");
        }

        return sub;
    }
}