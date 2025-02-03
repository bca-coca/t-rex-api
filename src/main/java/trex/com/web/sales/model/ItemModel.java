package trex.com.web.sales.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class ItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(length = 500)
    private String specification;

    @NotBlank(message = "Price is required")
    @Column(nullable = false)
    private String price;

    @NotNull(message = "At least one image is required")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "item_images",
        joinColumns = @JoinColumn(name = "item_id"),
        foreignKey = @ForeignKey(name = "fk_item_images")
    )
    @Column(name = "image_url", nullable = false)
    @Builder.Default
    private List<String> img = new ArrayList<>();
}