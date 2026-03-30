package com.loka.productservice.service;

import com.loka.productservice.dto.ProductDTO;
import com.loka.productservice.model.Product;
import com.loka.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements CommandLineRunner {

    private final ProductRepository productRepository;

    // ── Seed on startup ───────────────────────────────
    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            seedProducts();
            log.info("✓ {} products seeded into database", productRepository.count());
        }
    }

    private void seedProducts() {
        List<Product> products = Arrays.asList(
            Product.builder()
                .name("Silk Evening Gown")
                .description("A masterpiece of couture craftsmanship. This silk evening gown flows with effortless grace, featuring hand-stitched details and a silhouette that celebrates the feminine form. Perfect for black-tie events and gala evenings.")
                .price(new BigDecimal("24500"))
                .originalPrice(new BigDecimal("32000"))
                .category("Women")
                .imageUrl("https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=500&h=600&fit=crop")
                .featured(true)
                .material("100% Mulberry Silk")
                .careInstructions("Dry clean only")
                .sizes("XS,S,M,L,XL")
                .stockQuantity(50)
                .build(),

            Product.builder()
                .name("Cashmere Overcoat")
                .description("Crafted from the finest Grade-A Mongolian cashmere, this overcoat is the definition of refined elegance. Double-breasted silhouette with hand-finished edges and signature horn buttons.")
                .price(new BigDecimal("38000"))
                .category("Men")
                .imageUrl("https://images.unsplash.com/photo-1490578474895-699cd4e2cf59?w=500&h=600&fit=crop")
                .featured(true)
                .material("100% Mongolian Cashmere")
                .careInstructions("Dry clean recommended")
                .sizes("S,M,L,XL,XXL")
                .stockQuantity(30)
                .build(),

            Product.builder()
                .name("Leather Tote Bag")
                .description("Structured Italian leather tote with gold-plated hardware. Spacious interior with suede lining, multiple compartments, and a secure top zip. An investment piece for everyday luxury.")
                .price(new BigDecimal("15800"))
                .category("Accessories")
                .imageUrl("https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=500&h=600&fit=crop")
                .featured(true)
                .material("Full-grain Italian leather")
                .careInstructions("Condition with leather balm")
                .sizes("One Size")
                .stockQuantity(40)
                .build(),

            Product.builder()
                .name("Gold Chain Bracelet")
                .description("18K gold-plated chain bracelet with a lobster clasp. Delicate yet striking, this piece adds understated luxury to any ensemble.")
                .price(new BigDecimal("8900"))
                .category("Accessories")
                .imageUrl("https://images.unsplash.com/photo-1611652022419-a9419f74343d?w=500&h=600&fit=crop")
                .featured(false)
                .material("18K Gold Plated Brass")
                .careInstructions("Avoid contact with water and perfume")
                .sizes("One Size")
                .stockQuantity(75)
                .build(),

            Product.builder()
                .name("Merino Wool Blazer")
                .description("A contemporary silhouette in the finest Italian merino wool. Fully lined, with satin-covered buttons and a single back vent. The cornerstone of a modern wardrobe.")
                .price(new BigDecimal("19500"))
                .originalPrice(new BigDecimal("25000"))
                .category("Women")
                .imageUrl("https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=500&h=600&fit=crop")
                .featured(true)
                .material("100% Italian Merino Wool")
                .careInstructions("Dry clean only")
                .sizes("XS,S,M,L,XL")
                .stockQuantity(45)
                .build(),

            Product.builder()
                .name("Oxford Leather Shoes")
                .description("Hand-welted Oxford shoes crafted from full-grain calf leather. A Goodyear welt construction ensures longevity, while the leather sole develops character with every wear.")
                .price(new BigDecimal("12400"))
                .category("Men")
                .imageUrl("https://images.unsplash.com/photo-1533867617858-e7b97e060509?w=500&h=600&fit=crop")
                .featured(false)
                .material("Full-grain Calf Leather")
                .careInstructions("Polish regularly with leather cream")
                .sizes("6,7,8,9,10,11,12")
                .stockQuantity(60)
                .build(),

            Product.builder()
                .name("Chiffon Midi Dress")
                .description("Ethereal chiffon midi dress with a delicate floral print. Features a wrap front, adjustable tie waist, and flutter sleeves that move beautifully with every step.")
                .price(new BigDecimal("11200"))
                .category("Women")
                .imageUrl("https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=500&h=600&fit=crop")
                .featured(false)
                .material("100% Silk Chiffon")
                .careInstructions("Hand wash cold or dry clean")
                .sizes("XS,S,M,L")
                .stockQuantity(55)
                .build(),

            Product.builder()
                .name("Silk Pocket Square")
                .description("Hand-rolled silk pocket square with a classic geometric pattern. The perfect finishing touch to elevate any tailored look.")
                .price(new BigDecimal("3200"))
                .category("Accessories")
                .imageUrl("https://images.unsplash.com/photo-1583394838336-acd977736f90?w=500&h=600&fit=crop")
                .featured(false)
                .material("100% Silk Twill")
                .careInstructions("Dry clean only")
                .sizes("One Size")
                .stockQuantity(100)
                .build(),

            Product.builder()
                .name("Tailored Wool Trousers")
                .description("Precision-cut trousers in a mid-weight wool-blend fabric. Flat-front with a clean silhouette, side pockets, and a half-lining for comfort. The benchmark of modern tailoring.")
                .price(new BigDecimal("14500"))
                .category("Men")
                .imageUrl("https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=500&h=600&fit=crop")
                .featured(false)
                .material("Wool-Polyester Blend")
                .careInstructions("Dry clean recommended")
                .sizes("28,30,32,34,36,38")
                .stockQuantity(70)
                .build(),

            Product.builder()
                .name("Satin Clutch Bag")
                .description("Evening clutch in lustrous satin with a gold-tone frame closure and detachable chain strap. Lined in ivory silk with a compact mirror. Made to accompany you to life's finest moments.")
                .price(new BigDecimal("7200"))
                .category("Accessories")
                .imageUrl("https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=500&h=600&fit=crop")
                .featured(false)
                .material("Satin with Gold Tone Hardware")
                .careInstructions("Store in dust bag, avoid direct sunlight")
                .sizes("One Size")
                .stockQuantity(35)
                .build(),

            Product.builder()
                .name("Linen Summer Dress")
                .description("Relaxed linen dress with a square neckline and smocked bodice. Breathable and effortlessly chic, it transitions seamlessly from the beach to a terrace dinner.")
                .price(new BigDecimal("9800"))
                .category("Women")
                .imageUrl("https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500&h=600&fit=crop")
                .featured(false)
                .material("100% European Linen")
                .careInstructions("Machine wash cold, hang dry")
                .sizes("XS,S,M,L,XL")
                .stockQuantity(80)
                .build(),

            Product.builder()
                .name("Cashmere Turtleneck")
                .description("Ultra-fine two-ply cashmere turtleneck. Featherlight yet incredibly warm, with a ribbed cuff and hem for a refined finish. A luxury staple for cooler seasons.")
                .price(new BigDecimal("8600"))
                .originalPrice(new BigDecimal("12000"))
                .category("Men")
                .imageUrl("https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=500&h=600&fit=crop")
                .featured(false)
                .material("2-ply Grade-A Cashmere")
                .careInstructions("Hand wash cold or dry clean")
                .sizes("S,M,L,XL")
                .stockQuantity(65)
                .build()
        );

        productRepository.saveAll(products);
    }

    // ── Service Methods ───────────────────────────────

    public List<ProductDTO> getAllProducts(String category, String search,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           Boolean featured) {
        List<Product> products;

        if (Boolean.TRUE.equals(featured)) {
            products = productRepository.findByFeaturedTrueAndActiveTrueOrderByCreatedAtDesc();
        } else if (search != null && !search.isBlank()) {
            products = productRepository.searchProducts(search.trim());
        } else if (category != null && !category.isBlank() && !category.equals("All")) {
            products = productRepository.findByCategoryAndActiveTrueOrderByCreatedAtDesc(category);
        } else {
            products = productRepository.findByActiveTrueOrderByCreatedAtDesc();
        }

        // Apply price filter
        if (minPrice != null || maxPrice != null) {
            BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
            BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("9999999");
            products = products.stream()
                .filter(p -> p.getPrice().compareTo(min) >= 0 && p.getPrice().compareTo(max) <= 0)
                .collect(Collectors.toList());
        }

        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
    }

    public List<String> getCategories() {
        return productRepository.findDistinctCategories();
    }

    private ProductDTO toDTO(Product p) {
        int discount = 0;
        if (p.getOriginalPrice() != null && p.getOriginalPrice().compareTo(BigDecimal.ZERO) > 0) {
            discount = (int) Math.round(
                (1 - p.getPrice().doubleValue() / p.getOriginalPrice().doubleValue()) * 100
            );
        }

        List<String> sizeList = p.getSizes() != null
            ? Arrays.asList(p.getSizes().split(","))
            : List.of();

        return ProductDTO.builder()
            .id(p.getId())
            .name(p.getName())
            .description(p.getDescription())
            .price(p.getPrice())
            .originalPrice(p.getOriginalPrice())
            .category(p.getCategory())
            .imageUrl(p.getImageUrl())
            .stockQuantity(p.getStockQuantity())
            .featured(p.getFeatured())
            .material(p.getMaterial())
            .careInstructions(p.getCareInstructions())
            .sizes(sizeList)
            .createdAt(p.getCreatedAt())
            .discount(discount > 0 ? discount : null)
            .build();
    }
}
