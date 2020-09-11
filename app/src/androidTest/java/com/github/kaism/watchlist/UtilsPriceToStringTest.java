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
public class UtilsPriceToStringTest {

	@Parameters
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"122.78", 12278},
				{"50.00", 5000},
				{"2.01", 201},
				{"0.78", 78},
				{"0.08", 8},
				{"0.00", 0}
		});
	}

	private final String string;
	private final int price;

	public UtilsPriceToStringTest(String str, int num) {
		string = str;
		price = num;
	}

	@Test
	public void priceToStringTest() {
		assertThat(Utils.priceToString(price), is(equalTo(string)));
	}

}