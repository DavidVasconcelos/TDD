package br.ce.wcaquino.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static java.util.Calendar.DAY_OF_MONTH;

public class DiferencaDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer diferencaDias;

    public DiferencaDiasMatcher(Integer diferencaDias) {
        this.diferencaDias = diferencaDias;
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return isMesmaData(date, obterDataComDiferencaDias(diferencaDias));
    }

    @Override
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.add(DAY_OF_MONTH, diferencaDias);
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        description.appendText(dataExtenso);

    }
}
