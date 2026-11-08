package mishaps.content;

import arc.graphics.*;
import mindustry.type.*;
import mishaps.graphics.*;

public class MishapItems{

    public static Item rosingAlloy, diseaseFragments, diseaseVector, aquamarine;

    public static void load(){

        rosingAlloy = new Item("rosing-alloy", Color.valueOf("AF6356")){{
            cost = 1.2f;
        }};

        diseaseFragments = new Item("disease-fragments", MishapPal.darkDreadRust){{
            hardness = 4;
            buildable = false;
        }};

        diseaseVector = new Item("disease-vector", MishapPal.dreadRust);
        
        aquamarine = new Item("aquamarine", Color.valueOf("68ACF5")){{
            cost = 1.3f;
            hardness = 5;
            healthScaling = 0.3f;
        }};
    }
}
