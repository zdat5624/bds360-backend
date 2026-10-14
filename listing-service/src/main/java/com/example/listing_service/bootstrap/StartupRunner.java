package com.example.listing_service.bootstrap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.listing_service.common.constant.ListingType;
import com.example.listing_service.modules.address.dto.request.CreateDistrictRequest;
import com.example.listing_service.modules.address.dto.request.CreateProvinceRequest;
import com.example.listing_service.modules.address.dto.request.CreateWardRequest;
import com.example.listing_service.modules.address.entity.District;
import com.example.listing_service.modules.address.entity.Province;
import com.example.listing_service.modules.address.entity.Ward;
import com.example.listing_service.modules.address.repository.DistrictRepository;
import com.example.listing_service.modules.address.repository.ProvinceRepository;
import com.example.listing_service.modules.address.repository.WardRepository;
import com.example.listing_service.modules.address.service.MapboxGeocodeService;
import com.example.listing_service.modules.category.entity.Category;
import com.example.listing_service.modules.category.repository.CategoryRepository;
import com.example.listing_service.modules.post.constant.CompassDirection;
import com.example.listing_service.modules.post.constant.Furnishing;
import com.example.listing_service.modules.post.constant.LegalStatus;
import com.example.listing_service.modules.post.constant.PostStatus;
import com.example.listing_service.modules.post.entity.Image;
import com.example.listing_service.modules.post.entity.ListingDetail;
import com.example.listing_service.modules.post.entity.Post;
import com.example.listing_service.modules.post.repository.PostRepository;
import com.example.listing_service.modules.post.repository.PostViewHistoryRepository;
import com.example.listing_service.modules.post.repository.SavedPostRepository;
import com.example.listing_service.modules.vip.entity.Vip;
import com.example.listing_service.modules.vip.repository.VipRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final VipRepository vipRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final MapboxGeocodeService mapboxGeocodeService;
    private final JdbcTemplate jdbcTemplate;
    private final PostViewHistoryRepository postViewHistoryRepository;
    private final SavedPostRepository savedPostRepository;

    @Override
    public void run(String... args) {
        log.info(">>> START INIT LISTING DATABASE");

        initSampleAddress();
        initSampleCategories();
        initSampleVips();
        initSamplePosts();
        initSampleInteractions();

        log.info(">>> END INIT LISTING DATABASE");
    }

    private void initSampleAddress() {
        if (this.provinceRepository.count() > 0 || this.districtRepository.count() > 0 || this.wardRepository.count() > 0) {
            log.info(">>> SKIP! INIT ADDRESS DATA: ALREADY HAVE DATA ... ");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream inputStream = new ClassPathResource("data/address.json").getInputStream()) {
                List<CreateProvinceRequest> provinceDTOs = objectMapper.readValue(inputStream, new TypeReference<List<CreateProvinceRequest>>() {});
                for (CreateProvinceRequest provinceDTO : provinceDTOs) {
                    Province province = convertToEntity(provinceDTO);
                    provinceRepository.save(province);
                }
                log.info(">>> INIT ADDRESS DATA TABLE provinces, districs, wards: SUCCESS");
            }
        } catch (Exception e) {
            log.error("Error seeding addresses", e);
        }
    }

    private void initSampleCategories() {
        if (categoryRepository.count() > 0) {
            log.info(">>> SKIP! INIT CATEGORIES: ALREADY HAVE DATA ... ");
            return;
        }

        ArrayList<Category> lst = new ArrayList<>();
        lst.add(new Category("Cho thuê căn hộ chung cư", ListingType.RENT));
        lst.add(new Category("Cho thuê chung cư mini, căn hộ dịch vụ", ListingType.RENT));
        lst.add(new Category("Cho thuê nhà riêng", ListingType.RENT));
        lst.add(new Category("Cho thuê nhà biệt thự, liền kề", ListingType.RENT));
        lst.add(new Category("Cho thuê nhà mặt phố", ListingType.RENT));
        lst.add(new Category("Cho thuê shophouse, nhà phố thương mại", ListingType.RENT));
        lst.add(new Category("Cho thuê nhà trọ, phòng trọ", ListingType.RENT));
        lst.add(new Category("Cho thuê văn phòng", ListingType.RENT));
        lst.add(new Category("Cho thuê, sang nhượng cửa hàng, ki ốt", ListingType.RENT));
        lst.add(new Category("Cho thuê kho, nhà xưởng, đất", ListingType.RENT));
        lst.add(new Category("Cho thuê loại bất động sản khác", ListingType.RENT));

        lst.add(new Category("Bán căn hộ chung cư", ListingType.SALE));
        lst.add(new Category("Bán chung cư mini, căn hộ dịch vụ", ListingType.SALE));
        lst.add(new Category("Bán nhà riêng", ListingType.SALE));
        lst.add(new Category("Bán nhà biệt thự, liền kề", ListingType.SALE));
        lst.add(new Category("Bán nhà mặt phố", ListingType.SALE));
        lst.add(new Category("Bán shophouse, nhà phố thương mại", ListingType.SALE));
        lst.add(new Category("Bán đất nền dự án", ListingType.SALE));
        lst.add(new Category("Bán đất", ListingType.SALE));
        lst.add(new Category("Bán trang trại, khu nghỉ dưỡng", ListingType.SALE));
        lst.add(new Category("Bán condotel", ListingType.SALE));
        lst.add(new Category("Bán kho, nhà xưởng", ListingType.SALE));
        lst.add(new Category("Bán loại bất động sản khác", ListingType.SALE));

        this.categoryRepository.saveAll(lst);
        log.info(">>> INIT CATEGORIES: SUCCESS");
    }

    private void initSampleVips() {
        if (vipRepository.count() > 0) {
            log.info(">>> SKIP! INIT VIPS: ALREADY HAVE DATA ... ");
            return;
        }

        ArrayList<Vip> lst = new ArrayList<>();
        lst.add(new Vip(0, "VIP 0", 0));
        lst.add(new Vip(1, "VIP 1", 2000));
        lst.add(new Vip(2, "VIP 2", 4000));

        this.vipRepository.saveAll(lst);
        log.info(">>> INIT VIPS: SUCCESS");
    }

    // ====================================================================
    // ĐỌC NGƯỜI DÙNG TỪ FILE CSV ĐỂ LÀM MOCK DATA
    // ====================================================================
    private List<MockUser> loadMockUsersFromCsv() {
        List<MockUser> mockUsers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/users.csv").getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }

                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < cols.length; i++) cols[i] = cols[i].replaceAll("^\"|\"$", "");
                
                mockUsers.add(new MockUser(
                    Long.parseLong(cols[0]), 
                    cols[6], 
                    cols[9], 
                    cols[11], 
                    cols[2]
                ));
            }
        } catch (Exception e) {
            log.error(">>> LỖI ĐỌC MOCK DATA TỪ CSV", e);
        }
        return mockUsers;
    }

    private void initSamplePosts() {
        if (postRepository.count() > 0) {
            log.info(">>> SKIP! INIT POSTS: ALREADY HAVE DATA ... ");
            return;
        }

        List<MockUser> users = loadMockUsersFromCsv();
        if (users.isEmpty()) {
            log.error(">>> LỖI: KHÔNG CÓ USER NÀO TỪ CSV ĐỂ SEED POSTS!");
            return;
        }

        List<Category> categories = categoryRepository.findAll();
        List<Province> provinces = provinceRepository.findAll();
        List<Vip> vips = vipRepository.findAll();

        Vip vip0 = vips.stream().filter(v -> v.getVipLevel() == 0).findFirst().orElse(vips.get(0));
        Vip vip1 = vips.stream().filter(v -> v.getVipLevel() == 1).findFirst().orElse(vips.get(1));

        Province hcmcProvince = provinces.stream()
                .filter(p -> p.getCode() == 79L)
                .findFirst()
                .orElse(provinces.get(0));

        final List<Province> finalOtherProvinces = provinces.stream()
                .filter(p -> p.getCode() != 79L)
                .collect(Collectors.toList());

        CompassDirection[] orientations = CompassDirection.values();
        LegalStatus[] legalStatuses = LegalStatus.values();
        Furnishing[] furnishings = Furnishing.values();

        String baseImageUrl = "https://res.cloudinary.com/dwdehgtoq/image/upload/f_auto,q_auto/";
        int totalPost = 1000;
        int numberOfThreads = 10;

        Map<Long, List<District>> districtCache = new ConcurrentHashMap<>();
        Map<Long, List<Ward>> wardCache = new ConcurrentHashMap<>();
        Map<String, double[]> geocodeCache = new ConcurrentHashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        log.info(">>> SEEDING 1000 POSTS: 85% HCMC Clusters | 15% Scattered National | Bulletproof Logic...");

        for (int t = 0; t < numberOfThreads; t++) {
            final int threadIndex = t;
            executorService.submit(() -> {
                var random = java.util.concurrent.ThreadLocalRandom.current();
                List<Post> posts = new ArrayList<>();

                Category hcmClusterCat = categories.get(random.nextInt(categories.size()));
                District hcmClusterDist = null;
                Ward hcmClusterWard = null;
                int hcmClusterSize = (random.nextInt(100) < 70) ? (8 + random.nextInt(8)) : (3 + random.nextInt(5));
                int hcmCurrentCount = 0;

                int postsPerThread = totalPost / numberOfThreads;

                for (int i = 1; i <= postsPerThread; i++) {
                    boolean isHcm = random.nextInt(100) < 85;

                    Province selectedProv;
                    District selectedDist;
                    Ward selectedWard;
                    Category selectedCat;

                    if (isHcm) {
                        if (hcmCurrentCount >= hcmClusterSize || hcmClusterDist == null) {
                            hcmCurrentCount = 0;
                            hcmClusterSize = (random.nextInt(100) < 70) ? (8 + random.nextInt(8)) : (3 + random.nextInt(5));
                            hcmClusterCat = categories.get(random.nextInt(categories.size()));

                            List<District> dists = districtCache.computeIfAbsent(hcmcProvince.getCode(),
                                    k -> districtRepository.findByProvinceCode(k));
                            hcmClusterDist = dists.isEmpty() ? null : dists.get(random.nextInt(dists.size()));
                            if (hcmClusterDist != null) {
                                List<Ward> wards = wardCache.computeIfAbsent(hcmClusterDist.getCode(),
                                        k -> wardRepository.findByDistrictCode(k));
                                hcmClusterWard = wards.isEmpty() ? null : wards.get(random.nextInt(wards.size()));
                            }
                        }
                        selectedProv = hcmcProvince;
                        selectedDist = hcmClusterDist;
                        selectedWard = hcmClusterWard;
                        selectedCat = hcmClusterCat;
                        hcmCurrentCount++;
                    } else {
                        selectedProv = finalOtherProvinces.get(random.nextInt(finalOtherProvinces.size()));
                        List<District> dists = districtCache.computeIfAbsent(selectedProv.getCode(),
                                k -> districtRepository.findByProvinceCode(k));
                        selectedDist = dists.isEmpty() ? null : dists.get(random.nextInt(dists.size()));
                        selectedWard = null;
                        if (selectedDist != null) {
                            List<Ward> wards = wardCache.computeIfAbsent(selectedDist.getCode(),
                                    k -> wardRepository.findByDistrictCode(k));
                            selectedWard = wards.isEmpty() ? null : wards.get(random.nextInt(wards.size()));
                        }
                        selectedCat = categories.get(random.nextInt(categories.size()));
                    }

                    if (selectedDist == null) {
                        if (isHcm) hcmCurrentCount = hcmClusterSize; 
                        i--;
                        continue;
                    }

                    Post post = new Post();

                    long randomDaysAgo = (random.nextInt(100) < 40) ? random.nextLong(60) : random.nextLong(365);
                    Instant fakeCreatedAt = Instant.now().minus(randomDaysAgo, ChronoUnit.DAYS);
                    int durationDays = (randomDaysAgo < 60) ? 90 : (random.nextBoolean() ? 30 : 60);
                    Instant fakeExpireDate = fakeCreatedAt.plus(durationDays, ChronoUnit.DAYS);
                    boolean isActuallyExpired = fakeExpireDate.isBefore(Instant.now());

                    double roundedArea = Math.round((45 + random.nextDouble() * 180) * 10.0) / 10.0;
                    post.setArea(roundedArea);
                    long basePrice = (selectedCat.getType() == ListingType.RENT)
                            ? (3_000_000L + random.nextLong(20_000_000L))
                            : (long) (roundedArea * (20_000_000L + random.nextLong(60_000_000L)));
                    double priceFactor = Math.pow(1 - 0.08, randomDaysAgo / 365.0) * (0.8 + random.nextDouble() * 0.4);
                    post.setPrice((long) (basePrice * priceFactor));

                    post.setProvince(selectedProv);
                    post.setDistrict(selectedDist);
                    post.setWard(selectedWard);
                    post.setStreetAddress("Số " + (random.nextInt(500) + 1) + " Đường "
                            + STREET_NAMES.get(random.nextInt(STREET_NAMES.size())));

                    String geocodeSearchKey = String.format("%s, %s, %s",
                            (selectedWard != null ? selectedWard.getName() : ""), selectedDist.getName(),
                            selectedProv.getName());
                    double[] coords = geocodeCache.get(geocodeSearchKey);

                    if (coords == null) {
                        var latLngOpt = mapboxGeocodeService.getLatLngFromAddress(geocodeSearchKey);
                        if (latLngOpt.isPresent()) {
                            coords = latLngOpt.get();
                            geocodeCache.put(geocodeSearchKey, coords);
                        } else {
                            String fallbackKey = selectedDist.getName() + ", " + selectedProv.getName();
                            coords = geocodeCache.computeIfAbsent(fallbackKey,
                                    k -> mapboxGeocodeService.getLatLngFromAddress(k)
                                            .orElse(new double[] { 106.6297, 10.8231 }));
                        }
                    }
                    if (coords != null) {
                        double radiusInDegrees = (400.0 + random.nextInt(1500)) / 111_000.0;
                        double u = random.nextDouble(), v = random.nextDouble();
                        double w = radiusInDegrees * Math.sqrt(u), theta = 2 * Math.PI * v;
                        post.setLongitude(coords[0] + (w * Math.cos(theta)) / Math.cos(Math.toRadians(coords[1])));
                        post.setLatitude(coords[1] + (w * Math.sin(theta)));
                    }

                    if (isActuallyExpired) {
                        post.setStatus(PostStatus.EXPIRED);
                        long secondsActive = fakeExpireDate.getEpochSecond() - fakeCreatedAt.getEpochSecond();
                        post.setPushedAt(fakeCreatedAt.plusSeconds(random.nextLong(Math.max(1, secondsActive))));
                        post.setVip(vips.get(random.nextInt(vips.size())));
                    } else {
                        int statusRand = random.nextInt(100);
                        if (statusRand < 60) {
                            post.setStatus(PostStatus.APPROVED);
                            post.setPushedAt(Instant.now().minusSeconds(random.nextLong(172800)));
                            post.setVip(vips.get(random.nextInt(vips.size())));
                        } else if (statusRand < 75) {
                            post.setStatus(PostStatus.REVIEW_LATER);
                            post.setPushedAt(Instant.now().minusSeconds(random.nextLong(432000)));
                            post.setVip(vip1);
                        } else {
                            if (statusRand < 90)
                                post.setStatus(PostStatus.PENDING);
                            else if (statusRand < 95)
                                post.setStatus(PostStatus.REJECTED);
                            else
                                post.setStatus(PostStatus.BLOCKED);
                            post.setPushedAt(fakeCreatedAt);
                            post.setVip(vip0);
                        }
                    }

                    if (post.getStatus() == PostStatus.APPROVED && post.getVip().getVipLevel() > 0) {
                        post.setPushedAt(Instant.now().minusSeconds(random.nextLong(7200)));
                    }

                    // ========================================================
                    // GẮN DỮ LIỆU TÁC GIẢ TỪ FILE MOCK (SOFT-LINK)
                    // ========================================================
                    MockUser mockUser = users.get(random.nextInt(users.size()));
                    post.setUserId(mockUser.getId());
                    post.setAuthorName(mockUser.getName());
                    post.setAuthorPhone(mockUser.getPhone());
                    post.setAuthorAvatar(mockUser.getAvatar());
                    post.setCreatedBy(mockUser.getEmail());
                    
                    post.setNotifyOnView(random.nextBoolean()); // Điền thuộc tính notifyOnView

                    post.setCategory(selectedCat);
                    post.setType(selectedCat.getType());
                    post.setCreatedAt(fakeCreatedAt);
                    post.setExpireDate(fakeExpireDate);
                    post.setUpdatedAt(post.getPushedAt());
                    post.setDeletedByUser(false);
                    post.setView((long) (random.nextInt(16) + 30));

                    ListingDetail detail = new ListingDetail();
                    if (!selectedCat.getName().toLowerCase().contains("đất")) {
                        detail.setBedrooms(random.nextInt(4) + 1);
                        detail.setBathrooms(random.nextInt(2) + 1);
                        detail.setFurnishing(furnishings[random.nextInt(furnishings.length)]);
                    }
                    detail.setHouseDirection(orientations[random.nextInt(orientations.length)]);
                    detail.setLegalStatus(legalStatuses[random.nextInt(legalStatuses.length)]);
                    detail.setPost(post);
                    post.setListingDetail(detail);

                    String fullAddr = post.getStreetAddress() + ", "
                            + (selectedWard != null ? selectedWard.getName() + ", " : "") + selectedProv.getName();
                    populateContentAndImages(post, selectedCat.getName(), roundedArea, fullAddr, random, baseImageUrl,
                            (threadIndex * postsPerThread) + i);

                    posts.add(post);

                    if (posts.size() >= 100) {
                        synchronized (postRepository) {
                            postRepository.saveAll(posts);
                        }
                        posts.clear();
                    }
                }
                if (!posts.isEmpty()) {
                    synchronized (postRepository) {
                        postRepository.saveAll(posts);
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        log.info(">>> SEEDING POSTS COMPLETED!");
    }

    private void initSampleInteractions() {
        if (postViewHistoryRepository.count() > 0 || savedPostRepository.count() > 0) {
            log.info(">>> SKIP! INTERACTION DATA ALREADY EXISTS...");
            return;
        }

        List<Post> allPosts = postRepository.findAll();
        List<MockUser> allUsers = loadMockUsersFromCsv();
        
        if (allPosts.isEmpty() || allUsers.isEmpty()) return;

        List<Post> activePosts = allPosts.stream()
                .filter(p -> p.getStatus() == PostStatus.APPROVED || p.getStatus() == PostStatus.REVIEW_LATER)
                .collect(Collectors.toList());

        Instant now = Instant.now();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        log.info(">>> START SEEDING INTERACTIONS...");

        // 1. SEED LƯỢT XEM
        executor.submit(() -> {
            try {
                String sql = "INSERT INTO post_view_histories (post_id, user_id, ip_address, viewed_at) VALUES (?, ?, ?, ?)";
                List<Object[]> batchArgs = new ArrayList<>();
                ThreadLocalRandom random = ThreadLocalRandom.current();

                for (Post post : allPosts) {
                    int views = post.getView() != null ? post.getView().intValue() : 0;

                    for (int j = 0; j < views; j++) {
                        MockUser randomUser = allUsers.get(random.nextInt(allUsers.size()));
                        Long viewerId = (random.nextInt(100) < 30) ? randomUser.getId() : null;
                        Instant randomViewedAt = now.minusSeconds(random.nextLong(30L * 24 * 60 * 60));

                        batchArgs.add(new Object[] { post.getId(), viewerId, "192.168.1." + random.nextInt(255), randomViewedAt });

                        if (batchArgs.size() >= 1000) {
                            jdbcTemplate.batchUpdate(sql, batchArgs);
                            batchArgs.clear();
                        }
                    }
                }
                if (!batchArgs.isEmpty()) {
                    jdbcTemplate.batchUpdate(sql, batchArgs);
                }
                log.info(">>> SEEDING VIEWS: SUCCESSFUL!");
            } catch (Exception e) {
                log.error(">>> LỖI SEED VIEWS", e);
            }
        });

        // 2. SEED TIN ĐÃ LƯU
        executor.submit(() -> {
            try {
                if (activePosts.isEmpty()) return;
                String sql = "INSERT INTO saved_posts (user_id, post_id, saved_at) VALUES (?, ?, ?)";
                List<Object[]> batchArgs = new ArrayList<>();
                ThreadLocalRandom random = ThreadLocalRandom.current();

                for (MockUser user : allUsers) {
                    int toSave = random.nextInt(6) + 3;
                    Set<Long> pickedPostIds = new HashSet<>();
                    for (int k = 0; k < toSave; k++) {
                        Long postId = activePosts.get(random.nextInt(activePosts.size())).getId();
                        if (pickedPostIds.add(postId)) {
                            Instant randomSavedAt = now.minusSeconds(random.nextLong(30L * 24 * 60 * 60));
                            batchArgs.add(new Object[] { user.getId(), postId, randomSavedAt });
                        }
                        if (batchArgs.size() >= 500) {
                            jdbcTemplate.batchUpdate(sql, batchArgs);
                            batchArgs.clear();
                        }
                    }
                }
                if (!batchArgs.isEmpty())
                    jdbcTemplate.batchUpdate(sql, batchArgs);
                log.info(">>> SEEDING SAVED POSTS: SUCCESSFUL!");
            } catch (Exception e) {
                log.error(">>> LỖI SEED SAVED POSTS", e);
            }
        });

        executor.shutdown();
        try {
            if (!executor.awaitTermination(15, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private Province convertToEntity(CreateProvinceRequest provinceDTO) {
        Province province = new Province();
        province.setCode(provinceDTO.getCode());
        province.setName(provinceDTO.getName());
        province.setCodename(provinceDTO.getCodename());
        province.setDivisionType(provinceDTO.getDivisionType());
        province.setPhoneCode(provinceDTO.getPhoneCode());

        if (provinceDTO.getDistricts() != null) {
            List<District> districtEntities = new ArrayList<>();
            for (CreateDistrictRequest districtDTO : provinceDTO.getDistricts()) {
                District district = new District();
                district.setCode(districtDTO.getCode());
                district.setName(districtDTO.getName());
                district.setCodename(districtDTO.getCodename());
                district.setDivisionType(districtDTO.getDivisionType());
                district.setShortCodename(districtDTO.getShortCodename());
                district.setProvince(province);

                if (districtDTO.getWards() != null) {
                    List<Ward> wardEntities = new ArrayList<>();
                    for (CreateWardRequest wardDTO : districtDTO.getWards()) {
                        Ward ward = new Ward();
                        ward.setCode(wardDTO.getCode());
                        ward.setName(wardDTO.getName());
                        ward.setCodename(wardDTO.getCodename());
                        ward.setDivisionType(wardDTO.getDivisionType());
                        ward.setShortCodename(wardDTO.getShortCodename());
                        ward.setDistrict(district);
                        wardEntities.add(ward);
                    }
                    district.setWards(wardEntities);
                }
                districtEntities.add(district);
            }
            province.setDistricts(districtEntities);
        }
        return province;
    }

    // ====================================================================
    // HÀM HELPER ĐIỀN NỘI DUNG VÀ HÌNH ẢNH (KHỚP 100% LOGIC CŨ)
    // ====================================================================
    private void populateContentAndImages(Post post, String categoryName, double roundedArea, String fullAddress,
            Random random, String baseImageUrl, int loopIndex) {
        String title = "";
        String description = "";
        List<String> sampleImageUrls = new ArrayList<>();

        switch (categoryName) {
            case "Cho thuê căn hộ chung cư":
                title = String.format("Cho Thuê Căn Hộ Chung Cư 2 Phòng Ngủ %s m2 Gần Trung Tâm", roundedArea);
                description = String.format(
                        "Cho thuê căn hộ chung cư cao cấp, diện tích %s m2, gồm 2 phòng ngủ rộng rãi và 1 phòng khách thoáng mát. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chungcucaocap (1).jpg",
                        "chungcucaocap (2).jpg", "chungcucaocap (3).jpg", "chungcucaocap (4).jpg",
                        "chungcucaocap (5).jpg", "chungcucaocap (6).jpg", "canhochungcu (1).jpg",
                        "canhochungcu (2).jpg", "canhochungcu (3).jpg", "canhochungcu (4).jpg",
                        "canhochungcu (5).jpg", "canhochungcu (6).jpg", "canhochungcu (7).jpg",
                        "canhochungcu (8).jpg", "canhochungcu (9).jpg", "canhochungcu (10).jpg",
                        "canhochungcu (11).jpg");
                break;
            case "Cho thuê chung cư mini, căn hộ dịch vụ":
                title = String.format("Cho Thuê Chung Cư Mini %s m2 Gần Trường Đại Học", roundedArea);
                description = String.format(
                        "Cho thuê chung cư mini tiện nghi, diện tích %s m2, gồm 1 phòng ngủ ấm cúng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chungcu (1).jpg",
                        "chungcu (2).jpg", "chungcu (3).jpg", "chungcu (4).jpg", "chungcu (5).jpg",
                        "chungcu (6).jpg", "canhochungcumini (1).jpg", "canhochungcumini (2).jpg",
                        "canhochungcumini (3).jpg", "canhochungcumini (4).jpg", "canhochungcumini (5).jpg",
                        "canhochungcumini (6).jpg", "canhochungcumini (7).jpg", "canhochungcumini (8).jpg",
                        "canhochungcumini (9).jpg", "canhochungcumini (10).jpg", "canhochungcumini (11).jpg",
                        "canhochungcumini (12).jpg", "canhochungcumini (13).jpg", "canhochungcumini (14).jpg");
                break;
            case "Cho thuê nhà riêng":
                title = String.format("Cho Thuê Nhà Riêng 3 Tầng %s m2 Có Gara Ô Tô", roundedArea);
                description = String.format(
                        "Nhà riêng cho thuê, 3 tầng khang trang, diện tích %s m2, gồm 4 phòng ngủ rộng rãi. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenharieng (1).jpg", "chothuenharieng (2).jpg", "chothuenharieng (3).jpg",
                        "chothuenharieng (4).jpg", "chothuenharieng (5).jpg", "chothuenharieng (6).jpg",
                        "chothuenharieng (7).jpg", "banthuenharieng (1).jpg", "banthuenharieng (2).jpg",
                        "banthuenharieng (3).jpg", "banthuenharieng (4).jpg", "banthuenharieng (5).jpg",
                        "banthuenharieng (6).jpg",
                        "banthuenharieng (7).jpg", "banthuenharieng (8).jpg", "banthuenharieng (9).jpg",
                        "banthuenharieng (10).jpg");
                break;
            case "Cho thuê nhà biệt thự, liền kề":
                title = String.format("Cho Thuê Biệt Thự Liền Kề %s m2 Có Hồ Bơi Riêng", roundedArea);
                description = String.format(
                        "Cho thuê biệt thự liền kề đẳng cấp, diện tích %s m2, thiết kế sang trọng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "bietthulienke (1).jpg", "bietthulienke (2).jpg", "bietthulienke (3).jpg",
                        "bietthulienke (4).jpg", "bietthulienke (5).jpg", "bietthulienke (6).jpg",
                        "bietthulienke (7).jpg", "bietthulienke (8).jpg", "bietthulienke (9).jpg");
                break;
            case "Cho thuê nhà mặt phố":
                title = String.format("Cho Thuê Nhà Mặt Phố %s m2 Vị Trí Kinh Doanh Đắc Địa", roundedArea);
                description = String.format(
                        "Nhà mặt phố cho thuê, diện tích %s m2, 2 tầng rộng rãi. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenhamatpho (1).jpg", "chothuenhamatpho (2).jpg", "chothuenhamatpho (3).jpg",
                        "chothuenhamatpho (4).jpg", "chothuenhamatpho (5).jpg", "chothuenhamatpho (6).jpg",
                        "chothuenhamatpho (7).jpg", "chothuenhamatpho (8).jpg", "chothuenhamatpho (9).jpg");
                break;
            case "Cho thuê shophouse, nhà phố thương mại":
                title = String.format("Cho Thuê Shophouse %s m2 Mặt Tiền Rộng Khu Đô Thị Mới", roundedArea);
                description = String.format(
                        "Cho thuê shophouse hiện đại, diện tích %s m2, thiết kế 3 tầng tối ưu. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothueshophouse (1).jpg", "chothueshophouse (2).jpg", "chothueshophouse (3).jpg",
                        "chothueshophouse (4).jpg", "chothueshophouse (5).jpg", "chothueshophouse (6).jpg",
                        "chothueshophouse (7).jpg", "chothueshophouse (8).jpg");
                break;
            case "Cho thuê nhà trọ, phòng trọ":
                title = String.format("Cho Thuê Phòng Trọ %s m2 Sạch Sẽ Gần Trường Đại Học", roundedArea);
                description = String.format(
                        "Phòng trọ cho thuê sạch sẽ, diện tích %s m2, thiết kế gọn gàng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "nhatrophongtro (1).jpg", "nhatrophongtro (2).jpg", "nhatrophongtro (3).jpg",
                        "nhatrophongtro (4).jpg", "nhatrophongtro (5).jpg", "nhatrophongtro (6).jpg",
                        "nhatrophongtro (7).jpg", "nhatrophongtro (8).jpg");
                break;
            case "Cho thuê văn phòng":
                title = String.format("Cho Thuê Văn Phòng %s m2 Hiện Đại Tại Trung Tâm", roundedArea);
                description = String.format(
                        "Văn phòng cho thuê chuyên nghiệp, diện tích %s m2, không gian mở hiện đại. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuevanphong (1).jpg", "chothuevanphong (2).jpg", "chothuevanphong (3).jpg",
                        "chothuevanphong (4).jpg", "chothuevanphong (5).jpg", "chothuevanphong (6).jpg",
                        "chothuevanphong (7).jpg", "chothuevanphong (8).jpg");
                break;
            case "Cho thuê, sang nhượng cửa hàng, ki ốt":
                title = String.format("Cho Thuê hoặc Sang Nhượng Cửa Hàng %s m2 Đang Kinh Doanh", roundedArea);
                description = String.format(
                        "Cho thuê hoặc sang nhượng cửa hàng, diện tích %s m2, mặt tiền rộng 4m. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuecuahankiot (1).jpg", "chothuecuahankiot (2).jpg", "chothuecuahankiot (3).jpg",
                        "chothuecuahankiot (4).jpg", "chothuecuahankiot (5).jpg", "chothuecuahankiot (6).jpg",
                        "chothuecuahankiot (7).jpg", "chothuecuahankiot (8).jpg", "chothuecuahankiot (9).jpg");
                break;
            case "Cho thuê kho, nhà xưởng, đất":
                title = String.format("Cho Thuê Kho Bãi %s m2 Gần Khu Công Nghiệp", roundedArea);
                description = String.format(
                        "Cho thuê kho bãi rộng rãi, diện tích %s m2, kết cấu khung thép chắc chắn. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenhakhonhaxuong (1).jpg", "chothuenhakhonhaxuong (2).jpg",
                        "chothuenhakhonhaxuong (3).jpg", "chothuenhakhonhaxuong (4).jpg",
                        "chothuenhakhonhaxuong (5).jpg", "chothuenhakhonhaxuong (6).jpg",
                        "chothuenhakhonhaxuong (7).jpg", "chothuenhakhonhaxuong (8).jpg");
                break;
            case "Cho thuê loại bất động sản khác":
                title = String.format("Cho Thuê Mặt Bằng Đa Năng %s m2 Linh Hoạt Sử Dụng", roundedArea);
                description = String.format(
                        "Cho thuê mặt bằng đa năng, diện tích %s m2, vị trí linh hoạt. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "batdongsankhac (1).jpg", "batdongsankhac (2).jpg", "batdongsankhac (3).jpg",
                        "batdongsankhac (4).jpg", "batdongsankhac (5).jpg");
                break;

            case "Bán căn hộ chung cư":
                title = String.format("Bán Căn Hộ Chung Cư 3 Phòng Ngủ %s m2 View Công Viên", roundedArea);
                description = String.format(
                        "Bán căn hộ chung cư cao cấp, diện tích %s m2, gồm 3 phòng ngủ rộng rãi. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chungcucaocap (1).jpg",
                        "chungcucaocap (2).jpg", "chungcucaocap (3).jpg", "chungcucaocap (4).jpg",
                        "chungcucaocap (5).jpg", "chungcucaocap (6).jpg", "canhochungcu (1).jpg",
                        "canhochungcu (2).jpg", "canhochungcu (3).jpg", "canhochungcu (4).jpg",
                        "canhochungcu (5).jpg", "canhochungcu (6).jpg", "canhochungcu (7).jpg",
                        "canhochungcu (8).jpg", "canhochungcu (9).jpg", "canhochungcu (10).jpg",
                        "canhochungcu (11).jpg");
                break;
            case "Bán chung cư mini, căn hộ dịch vụ":
                title = String.format("Bán Chung Cư Mini %s m2 Đang Cho Thuê Ổn Định", roundedArea);
                description = String.format(
                        "Bán chung cư mini sinh lời cao, diện tích %s m2, gồm 1 phòng ngủ khép kín. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chungcu (1).jpg",
                        "chungcu (2).jpg", "chungcu (3).jpg", "chungcu (4).jpg", "chungcu (5).jpg",
                        "chungcu (6).jpg", "canhochungcumini (1).jpg", "canhochungcumini (2).jpg",
                        "canhochungcumini (3).jpg", "canhochungcumini (4).jpg", "canhochungcumini (5).jpg",
                        "canhochungcumini (6).jpg", "canhochungcumini (7).jpg", "canhochungcumini (8).jpg",
                        "canhochungcumini (9).jpg", "canhochungcumini (10).jpg", "canhochungcumini (11).jpg",
                        "canhochungcumini (12).jpg", "canhochungcumini (13).jpg", "canhochungcumini (14).jpg");
                break;
            case "Bán nhà riêng":
                title = String.format("Bán Nhà Riêng 4 Tầng %s m2 Hẻm Xe Hơi Yên Tĩnh", roundedArea);
                description = String.format(
                        "Bán nhà riêng 4 tầng kiên cố, diện tích %s m2, gồm 3 phòng ngủ rộng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenharieng (1).jpg", "chothuenharieng (2).jpg", "chothuenharieng (3).jpg",
                        "chothuenharieng (4).jpg", "chothuenharieng (5).jpg", "chothuenharieng (6).jpg",
                        "chothuenharieng (7).jpg", "banthuenharieng (1).jpg", "banthuenharieng (2).jpg",
                        "banthuenharieng (3).jpg", "banthuenharieng (4).jpg", "banthuenharieng (5).jpg",
                        "banthuenharieng (6).jpg",
                        "banthuenharieng (7).jpg", "banthuenharieng (8).jpg", "banthuenharieng (9).jpg",
                        "banthuenharieng (10).jpg");
                break;
            case "Bán nhà biệt thự, liền kề":
                title = String.format("Bán Biệt Thự Liền Kề %s m2 5 Phòng Ngủ Sang Trọng", roundedArea);
                description = String.format(
                        "Bán biệt thự liền kề đẳng cấp, diện tích %s m2, thiết kế 4 tầng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "bietthulienke (1).jpg", "bietthulienke (2).jpg", "bietthulienke (3).jpg",
                        "bietthulienke (4).jpg", "bietthulienke (5).jpg", "bietthulienke (6).jpg",
                        "bietthulienke (7).jpg", "bietthulienke (8).jpg", "bietthulienke (9).jpg");
                break;
            case "Bán nhà mặt phố":
                title = String.format("Bán Nhà Mặt Phố 3 Tầng %s m2 Vị Trí Kinh Doanh Vàng", roundedArea);
                description = String.format(
                        "Bán nhà mặt phố 3 tầng, diện tích %s m2, mặt tiền 5m lý tưởng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenhamatpho (1).jpg", "chothuenhamatpho (2).jpg", "chothuenhamatpho (3).jpg",
                        "chothuenhamatpho (4).jpg", "chothuenhamatpho (5).jpg", "chothuenhamatpho (6).jpg",
                        "chothuenhamatpho (7).jpg", "chothuenhamatpho (8).jpg", "chothuenhamatpho (9).jpg");
                break;
            case "Bán shophouse, nhà phố thương mại":
                title = String.format("Bán Shophouse 4 Tầng %s m2 Khu Thương Mại Sầm Uất", roundedArea);
                description = String.format(
                        "Bán shophouse hiện đại, diện tích %s m2, 4 tầng tối ưu. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothueshophouse (1).jpg", "chothueshophouse (2).jpg", "chothueshophouse (3).jpg",
                        "chothueshophouse (4).jpg", "chothueshophouse (5).jpg", "chothueshophouse (6).jpg",
                        "chothueshophouse (7).jpg", "chothueshophouse (8).jpg");
                break;
            case "Bán đất nền dự án":
                title = String.format("Bán Đất Nền Dự Án %s m2 Hạ Tầng Hoàn Thiện", roundedArea);
                description = String.format(
                        "Bán đất nền dự án vị trí đẹp, diện tích %s m2, nằm trong khu đô thị mới. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "bandatnenduan (1).jpg", "bandatnenduan (2).jpg", "bandatnenduan (3).jpg",
                        "bandatnenduan (4).jpg", "bandatnenduan (5).jpg", "bandatnenduan (6).jpg",
                        "bandatnenduan (7).jpg", "bandatnenduan (8).jpg", "bandatnenduan (9).jpg",
                        "bandatnenduan (10).jpg");
                break;
            case "Bán đất":
                title = String.format("Bán Lô Đất %s m2 Mặt Tiền Đường Lớn Gần Trung Tâm", roundedArea);
                description = String.format(
                        "Bán lô đất đẹp, diện tích %s m2, mặt tiền đường lớn 10m. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "bandat (1).jpg", "bandat (2).jpg", "bandat (3).jpg", "bandat (4).jpg",
                        "bandat (5).jpg", "bandat (6).jpg", "bandat (7).jpg", "bandat (8).jpg",
                        "bandat (9).jpg", "bandat (10).jpg", "bandat (11).jpg", "bandat (12).jpg",
                        "bandat (13).jpg");
                break;
            case "Bán trang trại, khu nghỉ dưỡng":
                title = String.format("Bán Trang Trại Nghỉ Dưỡng %s m2 Có Ao Cá và Vườn Cây", roundedArea);
                description = String.format(
                        "Bán trang trại nghỉ dưỡng, diện tích %s m2, gồm nhà nghỉ 2 tầng. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "bantrangtrainghiduong (1).jpg", "bantrangtrainghiduong (2).jpg",
                        "bantrangtrainghiduong (3).jpg", "bantrangtrainghiduong (4).jpg",
                        "bantrangtrainghiduong (5).jpg", "bantrangtrainghiduong (6).jpg",
                        "bantrangtrainghiduong (7).jpg", "bantrangtrainghiduong (8).jpg",
                        "bantrangtrainghiduong (9).jpg", "bantrangtrainghiduong (10).jpg",
                        "bantrangtrainghiduong (11).jpg", "bantrangtrainghiduong (12).jpg");
                break;
            case "Bán condotel":
                title = String.format("Bán Condotel %s m2 View Biển Đẳng Cấp 5 Sao", roundedArea);
                description = String.format(
                        "Bán condotel view biển đẳng cấp, diện tích %s m2, gồm 2 phòng ngủ. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "condotel (1).jpg", "condotel (2).jpg", "condotel (3).jpg", "condotel (4).jpg",
                        "condotel (5).jpg", "condotel (6).jpg", "condotel (7).jpg", "condotel (8).jpg");
                break;
            case "Bán kho, nhà xưởng":
                title = String.format("Bán Nhà Xưởng %s m2 Gần Khu Công Nghiệp Có Điện 3 Pha", roundedArea);
                description = String.format(
                        "Bán nhà xưởng kiên cố, diện tích %s m2, khung thép chắc chắn. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "chothuenhakhonhaxuong (1).jpg", "chothuenhakhonhaxuong (2).jpg",
                        "chothuenhakhonhaxuong (3).jpg", "chothuenhakhonhaxuong (4).jpg",
                        "chothuenhakhonhaxuong (5).jpg", "chothuenhakhonhaxuong (6).jpg",
                        "chothuenhakhonhaxuong (7).jpg", "chothuenhakhonhaxuong (8).jpg");
                break;
            case "Bán loại bất động sản khác":
                title = String.format("Bán Tài Sản Đặc Biệt %s m2 Vị Trí Độc Đáo Đa Năng", roundedArea);
                description = String.format(
                        "Bán tài sản đặc biệt, diện tích %s m2, vị trí độc đáo. Địa chỉ: %s.",
                        roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "batdongsankhac (1).jpg", "batdongsankhac (2).jpg", "batdongsankhac (3).jpg",
                        "batdongsankhac (4).jpg", "batdongsankhac (5).jpg");
                break;
            default:
                title = String.format("Tin Đăng Mẫu %d - %s m2", loopIndex, roundedArea);
                description = String.format(
                        "Mô tả mặc định cho tin đăng mẫu %d, diện tích %s m2. Địa chỉ: %s.",
                        loopIndex, roundedArea, fullAddress);
                sampleImageUrls = Arrays.asList(
                        "https://example.com/images/default1.jpg",
                        "https://example.com/images/default2.jpg");
                break;
        }

        description = description + GENERAL_DESCRIPTION;
        post.setTitle(title);
        post.setDescription(description);

        List<Image> images = new ArrayList<>();
        int maxImages = Math.min(sampleImageUrls.size(), 12);
        int numberOfImages = random.nextInt(maxImages - 3) + 4; // Từ 4 đến maxImages
        List<String> availableImageUrls = new ArrayList<>(sampleImageUrls);

        for (int j = 0; j < numberOfImages; j++) {
            Image image = new Image();
            String selectedImageName = "";

            if (!availableImageUrls.isEmpty()) {
                int randomIndex = random.nextInt(availableImageUrls.size());
                selectedImageName = availableImageUrls.remove(randomIndex);
            } else {
                selectedImageName = sampleImageUrls.get(random.nextInt(sampleImageUrls.size()));
            }

            if (selectedImageName.startsWith("http")) {
                image.setUrl(selectedImageName);
            } else {
                // 🌟 FIX CHUẨN 100%: Biến "tên (số).jpg" thành "tên_số.jpg"
                String fixedName = selectedImageName
                        .replace(" (", "_") 
                        .replace(")", ""); 
                image.setUrl(baseImageUrl + fixedName);
            }

            image.setOrderIndex(j);
            image.setPost(post);
            images.add(image);
        }
        post.setImages(images);
    }

    // Class nội bộ hứng dữ liệu từ CSV
    private static class MockUser {
        private final Long id;
        private final String email;
        private final String name;
        private final String phone;
        private final String avatar;

        public MockUser(Long id, String email, String name, String phone, String avatar) {
            this.id = id; 
            this.email = email; 
            this.name = name; 
            this.phone = phone; 
            this.avatar = avatar;
        }
        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getAvatar() { return avatar; }
    }

    private static final List<String> STREET_NAMES = Arrays.asList(
            "A1", "A2", "A5", "A8", "A12", "A18", "A25", "A30", "A35", "A40",
            "A45", "A50", "B1", "B3", "B7", "B10", "B15", "B20", "B28", "B32",
            "B38", "B45", "B50", "C2", "C4", "C6", "C9", "C12", "C16", "C22",
            "C27", "C33", "C39", "C45", "C50", "D1", "D5", "D8", "D10", "D15",
            "D20", "D25", "D30", "D36", "D42", "D50", "E3", "E7", "E12", "E18",
            "E24", "E29", "E35", "E40", "E50", "F2", "F8", "F15", "F21", "F30",
            "F38", "F45", "G1", "G5", "G10", "G15", "G20", "G25", "G30", "G35",
            "G40", "G50", "H2", "H7", "H12", "H18", "H24", "H30", "H36", "H45",
            "I1", "I5", "I10", "I15", "I20", "I25", "I30", "I40", "J2", "J8",
            "J15", "J21", "J30", "J40", "K3", "K9", "K18", "K27", "K36", "K45");

    private static final String GENERAL_DESCRIPTION = "\n\n**Thông tin bổ sung**:\n" +
            "- Vị trí đắc địa: Nằm trong khu vực phát triển sôi động, xung quanh có đầy đủ tiện ích như trường học quốc tế, bệnh viện đa khoa, siêu thị lớn, công viên xanh mát và các trung tâm thương mại hiện đại.\n" +
            "- Giao thông thuận tiện: Gần các trục đường chính và tuyến giao thông huyết mạch, dễ dàng di chuyển đến trung tâm thành phố hoặc các khu vực lân cận trong thời gian ngắn.\n" +
            "- Tiện ích đa dạng: Cư dân được hưởng các tiện ích cao cấp như hồ bơi, phòng gym, khu vui chơi trẻ em, không gian BBQ ngoài trời, và hệ thống an ninh 24/7 đảm bảo sự an toàn tuyệt đối.\n" +
            "- Hỗ trợ toàn diện: Đội ngũ tư vấn chuyên nghiệp sẵn sàng hỗ trợ từ A-Z, bao gồm xem nhà miễn phí, tư vấn pháp lý nhanh chóng, và đàm phán giá tốt nhất để bạn có được giao dịch hoàn hảo.\n" +
            "- Cam kết chất lượng: Chúng tôi cung cấp thông tin minh bạch, chính xác, đảm bảo mọi chi tiết về bất động sản đều được kiểm tra kỹ lưỡng trước khi giới thiệu đến bạn.\n" +
            "- Cơ hội không thể bỏ lỡ: Hãy liên hệ ngay hôm nay để được tư vấn chi tiết, đặt lịch xem nhà thực tế, và nhận ưu đãi đặc biệt dành riêng cho khách hàng sớm nhất!";
}