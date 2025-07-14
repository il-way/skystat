package taf.data;

import vo.taf.Taf;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestData {

	protected Map<String, Taf> tafMap = new HashMap<>();

	public TestData() {
		init();
	}

	protected ZonedDateTime ofUTC(int month, int day, int hour, int min) {
		return ZonedDateTime.of(2025, month, day, hour, min, 0, 0, ZoneOffset.UTC);
	}

	private void init() {
		TestTafRKSS rkss = new TestTafRKSS();
		TestTafKJFK kjfk = new TestTafKJFK();
		TestTafLHSN lhsn = new TestTafLHSN();
		TestTafZBHH zbhh = new TestTafZBHH();
		tafMap.put("RKSS", rkss.getTaf());
		tafMap.put("KJFK", kjfk.getTaf());
		tafMap.put("LHSN", lhsn.getTaf());
		tafMap.put("ZBHH", zbhh.getTaf());
	}

}
