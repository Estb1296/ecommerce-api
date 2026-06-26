# E-Commerce API - Year Up Capstone 3
 
A full-featured Spring Boot REST API for an e-commerce platform with user authentication, product management, shopping cart, and order checkout functionality.
 
## Overview
 
This project demonstrates enterprise-level backend development using Spring Boot, Spring Security, Spring Data JPA, and MySQL. The API is fully decoupled with a layered architecture (Controller → Service → Repository → Database) and includes comprehensive unit and integration testing.
 
## Tech Stack
 
- **Framework**: Spring Boot 4.0.2
- **Security**: Spring Security 7.0.2 with JWT authentication
- **Database**: MySQL with Hibernate ORM
- **Testing**: JUnit 5, Spring Test, H2 (in-memory for tests)
- **Build**: Maven
- **Java**: JDK 11+
## Project Architecture
 
```
org.yearup
├── controllers/          # REST endpoints (@RestController)
├── service/              # Business logic (@Service)
├── repository/           # Data access (JpaRepository)
├── models/               # Entity classes (@Entity)
├── DTO/                  # Data Transfer Objects (search, detail)
├── exception/            # Custom exceptions
└── security/
    └── jwt/              # JWT token handling
```
 
## Features
 
### Phase 1: Categories Management
- **GET** `/categories` - List all categories
- **GET** `/categories/{id}` - Get specific category
- **GET** `/categories/{id}/products` - Get products in category
- **POST** `/categories` - Create category (admin only)
- **PUT** `/categories/{id}` - Update category (admin only)
- **DELETE** `/categories/{id}` - Delete category (admin only)
### Phase 2: Products & Bug Fixes
- **GET** `/products` - List/search products with filters
  - Query params: `cat` (categoryId), `minPrice`, `maxPrice`, `subCategory`
- **GET** `/products/{id}` - Get product details
- **POST** `/products` - Create product (admin only)
- **PUT** `/products/{id}` - Update product (admin only)
- **DELETE** `/products/{id}` - Delete product (admin only)
**Bugs Fixed:**
- ✅ Search functionality returning incomplete results
- ✅ Product stock field not updating correctly
### Phase 3: Shopping Cart
- **GET** `/cart` - Retrieve user's shopping cart (authenticated)
- **POST** `/cart/products/{productId}` - Add item to cart
- **PUT** `/cart/products/{productId}` - Update item quantity
- **DELETE** `/cart` - Clear entire cart
**Features:**
- Items persist in database per user
- Automatic quantity increment when adding duplicate product
- Returns cart with full product details and line totals
### Phase 4: User Profile
- **GET** `/profile` - View current user's profile (authenticated)
- **PUT** `/profile` - Update user's profile
**Auto-created on registration** with fields:
- firstName, lastName, email, phone, address, city, state, zip
### Phase 5: Checkout & Orders
- **POST** `/orders` - Convert cart to order (authenticated, no body)
**Process:**
1. Retrieves user's shopping cart
2. Creates Order record with user's profile address
3. Creates OrderItem for each cart item (captures price, discount, product name at time of purchase)
4. Clears shopping cart
5. Returns created order
## Key Design Patterns
 
### Decoupling Strategy
- **ShoppingCartItem**: In-memory model capturing product details at cart time
- **OrderItem**: Stores historical price data (not linked to Product entity)
- **ProductSearchDto / ProductDetailDto**: Database-level projections to avoid loading unnecessary data
### Service Layer Architecture
- Constructor injection with `private final` fields
- `@Transactional` for operations modifying multiple entities
- Custom exception handling (InvalidInputException, ResourceNotFoundException, DataAccessException)
- Input validation before business logic
### Security
- JWT token-based authentication in `JWTFilter`
- Role-based access control (`@PreAuthorize("hasRole('ADMIN')")`)
- Principal injected via `@AuthenticationPrincipal String username`
## Testing
 
### Repository Tests (@DataJpaTest)
- H2 in-memory database
- SQL scripts load test data via `@Sql`
- Tests for @Query methods and custom queries
- `@Transactional` with `BEFORE_TEST_CLASS` for isolation
```bash
mvn test -Dtest=ProductRepositoryTest
```
 
### Service Tests
- Mock repositories with Mockito
- Verify business logic and validations
### Integration Tests
- MockMvc for HTTP endpoint testing
- Full Spring context with test database
## Running the Project
 
### Build
```bash
mvn clean install
```
 
### Run Application
```bash
mvn spring-boot:run
```
 
Server starts on `http://localhost:8080`
 
### Run Tests
```bash
# All tests
mvn test
 
# Specific test class
mvn test -Dtest=ProductRepositoryTest
 
# Specific test method
mvn test -Dtest=ProductRepositoryTest#searchWithDto_noCriteria_shouldReturn_allProducts
```
 
## API Examples
 
### Authentication
```bash
POST http://localhost:8080/auth/register
Body: { "username": "user1", "password": "pass123" }
 
POST http://localhost:8080/auth/login
Body: { "username": "user1", "password": "pass123" }
Response: { "token": "eyJhbGciOiJIUzI1NiIs..." }
```
 
### Product Search
```bash
GET http://localhost:8080/products?cat=1&minPrice=50&maxPrice=500
GET http://localhost:8080/products?subCategory=Black
```
 
### Shopping Cart
```bash
POST http://localhost:8080/cart/products/5
GET http://localhost:8080/cart
PUT http://localhost:8080/cart/products/5?quantity=3
DELETE http://localhost:8080/cart
```
 
### Checkout
```bash
POST http://localhost:8080/orders
```
 
## Database Schema
 
**Key Tables:**
- `users` - Authentication credentials
- `profiles` - User profile information
- `categories` - Product categories
- `products` - Product inventory
- `shopping_cart` - User cart items (userId, productId, quantity)
- `orders` - Order records
- `order_line_items` - Order line items with historical pricing
## Key Learnings & Best Practices
 
✅ **Decoupling**: Services don't load unnecessary data; DTOs project only required fields  
✅ **Historical Data**: OrderItem stores price/name at purchase time, independent of Product changes  
✅ **Transaction Safety**: `@Transactional` ensures atomicity across multiple repository operations  
✅ **Security**: JWT tokens + role-based authorization on sensitive endpoints  
✅ **Testing**: Repository tests with @DataJpaTest, service tests with mocks, integration tests with MockMvc  
✅ **Error Handling**: Custom exceptions with meaningful messages, global exception handler  
✅ **Input Validation**: Checked before business logic, exceptions not swallowed in try-catch  
 
## Future Enhancements
 
- [ ] Payment processing integration
- [ ] Order status tracking (pending, shipped, delivered)
- [ ] Product reviews and ratings
- [ ] Email notifications on order
- [ ] Admin dashboard
- [ ] Pagination for large result sets
- [ ] Caching with Redis
## Notes
 
- Branch strategy: Main branch = stable, decouple branch = experimental refactoring
- All tests passing before merging changes
- Enterprise-level code quality with comprehensive test coverage

## Featured Code Snippets
### Rebuilding In-Memory Cart from Persistent Storage

```bash
public ShoppingCart getByUserId(int userId) {
    if (userId <= 0) {
        throw new InvalidInputException("User ID must be a positive number");
    }
    List cartItems = shoppingCartRepository.findByUserId(userId);
    ShoppingCart shoppingCart = new ShoppingCart();
    for (CartItem cartItem : cartItems) {
        Product product = productService.getById(cartItem.getProductId());
        if (product != null) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            shoppingCart.add(item);  // Adds to Map
        }
    }
    return shoppingCart;
}
```

- This method demonstrates the cart construction pattern: 
  - retrieving persisted CartItem rows, enriching each with product lookup, and projecting into a map-based ShoppingCart domain model.
  - Input validation and defensive null checks prevent invalid states, while the map structure enables efficient O(1) access to cart items by product ID.
### Decoupled Order Checkout with Historical Data

This demonstrates capturing product data at purchase time, completely decoupled from the Product entity:

```bash
for (ShoppingCartItem cartItem : cart.getItems().values()) {
    OrderItem orderItem = new OrderItem(
            savedOrder.getOrderId(),
            cartItem.getProductId(),
            cartItem.getProductName(),      // Captured at checkout time
            cartItem.getQuantity(),
            cartItem.getPrice(),             // Historical price, not current
            cartItem.getDiscountPercent()
    );
    orderItemRepository.save(orderItem);
}
```

**Why this matters:** If a product's price changes, the order still reflects what the customer paid. This is enterprise-level data integrity.
