package com.javarush.reviewplatform;

import com.javarush.reviewplatform.category.Category;
import com.javarush.reviewplatform.category.CategoryRepository;
import com.javarush.reviewplatform.product.Product;
import com.javarush.reviewplatform.product.ProductRepository;
import com.javarush.reviewplatform.review.Review;
import com.javarush.reviewplatform.review.ReviewRepository;
import com.javarush.reviewplatform.user.Role;
import com.javarush.reviewplatform.user.User;
import com.javarush.reviewplatform.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TestDataService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser() {
        User user = User.builder()
                .username("TestUser" + (System.currentTimeMillis() / 10000))
                .password(passwordEncoder.encode("password"))
                .firstName("first")
                .lastName("last")
                .email("test@email.ru")
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public User createUser(String name, String password) {
        User user = User.builder()
                .username(name)
                .password(passwordEncoder.encode(password))
                .firstName("first")
                .lastName("last")
                .email("test@email.ru")
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public Category createCategory() {
        Category category = Category.builder()
                .name("Game")
                .description("For games")
                .build();
        return categoryRepository.save(category);
    }

    public Product createProduct(Category category) {
        Product product = Product.builder()
                .title("TestProduct")
                .description("Test product")
                .category(category)
                .build();
        return productRepository.save(product);
    }

    public Review createReview(User user, Product product) {
        Review review = Review.builder()
                .user(user)
                .title("TestReview")
                .content("Test review")
                .rating(10)
                .product(product)
                .build();
        return reviewRepository.save(review);
    }
}
