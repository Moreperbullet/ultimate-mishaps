package mishaps.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mishaps.graphics.*;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class MishapFx{
    public static final Rand rand = new Rand();
    public static final Vec2 temp = new Vec2();
    public static Effect

    orangeLaserCharge = new Effect(80f, e -> {
        color(Color.valueOf("ec7458"));
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, 4f + e.fout() * 100f);

        Fill.circle(e.x, e.y, e.fin() * 20);

        randLenVectors(e.id, 20, 40f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5f);
            Drawf.light(e.x + x, e.y + y, e.fin() * 15f, Color.valueOf("ec7458"), 0.7f);
        });

        color();

        Fill.circle(e.x, e.y, e.fin() * 10);
        Drawf.light(e.x, e.y, e.fin() * 20f, Color.valueOf("ec7458"), 0.7f);
    }).followParent(true).rotWithParent(true),

    dreadRusting = new Effect(40f, e -> {
        color(MishapPal.dreadRust);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 1.2f);
        });
    }),

    hellburnt = new Effect(35f, e -> {
        color(MishapPal.lightHell, MishapPal.darkHell, e.fin());

        randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.5f);
        });
    }),

    dreadShoot = new Effect(12f, e -> {
        color(Color.white, MishapPal.dreadRust, e.fin());
        stroke(e.fout() * 1.2f + 0.5f);

        randLenVectors(e.id, 7, 25f * e.finpow(), e.rotation, 50f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin() * 5f + 2f);
        });
    }),

    railShootColor = new Effect(24f, e -> {
        e.scaled(10f, b -> {
            color(Color.white, Color.lightGray, b.fin());
            stroke(b.fout() * 3f + 0.2f);
            Lines.circle(b.x, b.y, b.fin() * 50f);
        });

        color(e.color);

        for(int i : Mathf.signs){
            Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
        }
    }),

    railTrailColor = new Effect(16f, e -> {
        color(e.color);

        for(int i : Mathf.signs){
            Drawf.tri(e.x, e.y, 10f * e.fout(), 24f, e.rotation + 90 + 90f * i);
        }

        Drawf.light(e.x, e.y, 60f * e.fout(), Pal.orangeSpark, 0.5f);
    }),

    railHitColor = new Effect(18f, 200f, e -> {
        color(e.color);

        for(int i : Mathf.signs){
            Drawf.tri(e.x, e.y, 10f * e.fout(), 60f, e.rotation + 140f * i);
        }
    });
}
