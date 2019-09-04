package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatcherProprios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DiferencaDiasMatcher ehHoje() {
        return new DiferencaDiasMatcher(0);
    }

    public static DiferencaDiasMatcher ehHojeComDiferencaDias(Integer diferenca) {
        return new DiferencaDiasMatcher(diferenca);
    }
}
