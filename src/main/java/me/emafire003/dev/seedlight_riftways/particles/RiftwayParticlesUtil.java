package me.emafire003.dev.seedlight_riftways.particles;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class RiftwayParticlesUtil {

    public static List<Vec3d> getRectanglePerimeter(double base, double side, Vec3d center, double base_distance, double side_distance, int particles_per_side){
        List<Vec3d> particle_locations = new ArrayList<>();

        //distance between each particles, only of the first side aka the base
        double dist_between_prcls_base = base/particles_per_side;

        //distance between each particles, only of the other side, aka the height
        double dist_between_prcls_side = side/particles_per_side;

        //centro.x + distanza, centro.x-distanza
        Vec3d mid_base_positive = center.add(base_distance, 0, 0);

        //Adds half of the total lenght of the base to the z of the midpoint, making the mid point the start point of the base
        Vec3d base_pos_start = mid_base_positive.add(0, 0, base/2);

        Vec3d base_pos_iter = base_pos_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(base_pos_iter);
            base_pos_iter = base_pos_iter.add(0,0,-dist_between_prcls_base);
        }

        Vec3d mid_base_negative = center.add(-base_distance, 0, 0);

        Vec3d base_neg_start = mid_base_negative.add(0, 0, -base/2);

        Vec3d base_neg_iter = base_neg_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(base_neg_iter);
            base_neg_iter = base_neg_iter.add(0,0, dist_between_prcls_base);
        }


        //SIDE CALCULATIONS


        Vec3d mid_side_positive = center.add(0, 0, side_distance);

        Vec3d side_pos_start = mid_side_positive.add(side/2, 0, 0);

        Vec3d side_pos_iter = side_pos_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(side_pos_iter);
            side_pos_iter = side_pos_iter.add(-dist_between_prcls_side,0, 0);
        }

        Vec3d mid_side_negative = center.add(0, 0, -side_distance);

        Vec3d side_neg_start = mid_side_negative.add(-side/2, 0, 0);

        Vec3d side_neg_iter = side_neg_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(side_neg_iter);
            side_neg_iter = side_neg_iter.add(dist_between_prcls_side,0, 0);
        }

        return particle_locations;
    }
    public static List<Vec3d> getRectanglePerimeterOld(double base, double side, Vec3d center, double base_distance, double side_distance, int particles_per_side){
        List<Vec3d> particle_locations = new ArrayList<>();

        //distance between each particles, only of the first side aka the base
        double dist_between_prcls_base = base/particles_per_side;

        //distance between each particles, only of the other side, aka the height
        double dist_between_prcls_side = side/particles_per_side;
        
        //centro.x + distanza, centro.x-distanza
        Vec3d mid_base_positive = center.add(base_distance, 0, 0);

        //Adds half of the total lenght of the base to the z of the midpoint, making the mid point the start point of the base
        Vec3d base_pos_start = mid_base_positive.add(0, 0, base/2);

        Vec3d base_pos_iter = base_pos_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(base_pos_iter);
            base_pos_iter = base_pos_iter.add(0,0,dist_between_prcls_base);
        }
        
        Vec3d mid_base_negative = center.add(-base_distance, 0, 0);
        
        Vec3d base_neg_start = mid_base_negative.add(0, 0, -base/2);

        Vec3d base_neg_iter = base_neg_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(base_neg_iter);
            base_neg_iter = base_neg_iter.add(0,0, -dist_between_prcls_base);
        }


        //SIDE CALCULATIONS


        Vec3d mid_side_positive = center.add(0, 0, side_distance);

        Vec3d side_pos_start = mid_side_positive.add(side/2, 0, 0);

        Vec3d side_pos_iter = side_pos_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(side_pos_iter);
            side_pos_iter = side_pos_iter.add(dist_between_prcls_side,0, 0);
        }

        Vec3d mid_side_negative = center.add(0, 0, -side_distance);

        Vec3d side_neg_start = mid_side_negative.add(-side/2, 0, 0);

        Vec3d side_neg_iter = side_neg_start;
        for(int i = 0; i<particles_per_side; i++){
            particle_locations.add(side_neg_iter);
            side_neg_iter = side_neg_iter.add(-dist_between_prcls_side,0, 0);
        }
        
        return particle_locations;
    }

}
