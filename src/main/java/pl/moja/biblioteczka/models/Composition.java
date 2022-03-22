package pl.moja.biblioteczka.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Composition implements BaseModel {

    private int symbolId;
    private String symbolName;
    private MaterialComposition materialComposition;

    public String getCompositionString() {
        float compostionRatioMin = materialComposition.getCompositionRatioMin();
        float compostionRatioMax = materialComposition.getCompositionRatioMax();

        if (compostionRatioMin != 0) {
            if (compostionRatioMax != 0) {
                return compostionRatioMin + " < " + symbolName + " < " + compostionRatioMax;
            } else {
                return compostionRatioMin + " < " + symbolName;
            }
        } else {
            if (compostionRatioMax != 0) {
                return symbolName + " < " + compostionRatioMax;
            }
        }


        return symbolName;
    }

}
