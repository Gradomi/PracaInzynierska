package pl.moja.biblioteczka.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MaterialComposition {

    private int materialId;
    private int chemicalCompositionId;
    private float compositionRatioMin;
    private float compositionRatioMax;

}
