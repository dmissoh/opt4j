package com.kiolis.optimizer.menu;

import com.kiolis.optimizer.menu.models.Menu;
import com.kiolis.optimizer.menu.models.Substance;
import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Decoder;

public class MenuDecoder implements Decoder<SelectGenotype<Substance>, Menu> {

    public Menu decode(SelectGenotype<Substance> genotype) {
        Menu menu = new Menu();
        for (int i = 0; i < genotype.size(); i++) {
            menu.add(genotype.getValue(i));
        }
        return menu;
    }

}