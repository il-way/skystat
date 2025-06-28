package taf;

import org.junit.jupiter.api.Test;
import service.TafSnapshotExpander;
import vo.taf.Taf;
import vo.taf.field.WeatherSnapshot;
import java.util.List;

public class TafTest extends TafTestData {

	@Test
	void print() {
		TafTestData tafTestData = new TafTestData();
		Taf taf = tafTestData.generate();

		TafSnapshotExpander expander = new TafSnapshotExpander();
		List<WeatherSnapshot> expanded = expander.expand(taf);
		System.out.println(expanded);

	}

}

