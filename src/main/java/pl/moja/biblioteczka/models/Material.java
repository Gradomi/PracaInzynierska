package pl.moja.biblioteczka.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Material implements BaseModel {

    private int id;
    private String materialName;
    private String country;
    private String norm;
    private BigDecimal yieldStrenghtMin;
    private BigDecimal yieldStrenghtMax;
    private BigDecimal tensileStrengthMin;
    private BigDecimal tensileStrengthMax;
    private BigDecimal percentageElongationMin;
    private BigDecimal percentageElongationMax;
    private BigDecimal brinellHardnessMin;
    private BigDecimal brinellHardnessMax;
    private BigDecimal impactStrengthMin;
    private BigDecimal impactStrengthMax;
    private Object materialComposition;
    private boolean favourite;

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setFavourite(int i) {
        this.favourite = i != 0;
    }

    public boolean isFavourite() {
        return this.favourite;
    }

    public int isFavouriteBinary(){
        return this.favourite ? 1 : 0;
    }

}
