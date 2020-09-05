package com.github.kaism.watchlist;


import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
@SmallTest
public class UtilsStringToPriceTest {

	@Parameters
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"122.78", 12278},
				{"862.2", 86220},
				{"123", 12300},
				{"37.55", 3755},
				{"21.5", 2150},
				{"74", 7400},
				{"8.71", 871},
				{"2.6", 260},
				{"5", 500},
				{"0.78", 78},
				{"0.5", 50},
				{"0", 0},
				{"0.6581", 66},
				{"0.0812", 8},
				{"0.0011", 0},
				{"0.0003", 0},
				{null, 0},
				{"", 0},
				{"beer", 0}
		});
	}

	private final String string;
	private final int price;

	public UtilsStringToPriceTest(String str, int num) {
		string = str;
		price = num;
	}

	@Test
	public void stringToPriceTest() {
		assertThat(Utils.stringToPrice(string), is(equalTo(price)));
	}
}