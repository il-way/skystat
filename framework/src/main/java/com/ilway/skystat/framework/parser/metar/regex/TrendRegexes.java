package com.ilway.skystat.framework.parser.metar.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.ilway.skystat.framework.parser.metar.regex.RemarkRegexes.remarkTokens;

@Getter
@RequiredArgsConstructor
public enum TrendRegexes {

	BECMG("becmg", getBecmgRegex()),
	PROB("prob", getProbRegex()),
	TEMPO("tempo", getTempoRegex()),
	INTER("inter", getInterRegex()),
	NOSIG("nosig", getNosigRegex()),
	FM("fm", getFmRegex()),
	TL("tl", getTlRegex()),
	AT("at", getAtRegex());

	private final String groupName;
	private final String regex;

	public static String fullPattern() {
		return String.format("(?:%s|%s|%s|%s|%s|%s|%s|%s)",
			getBecmgRegex(), getProbRegex(), getTempoRegex(),
			getInterRegex(), getNosigRegex(),
			getFmRegex(), getTlRegex(), getAtRegex()
		);
	}

	public static String segmentPattern() {
		// 다음 세그먼트/Remark/문자열 끝을 경계로 삼는 룩어헤드
		String boundaryLookahead =
			"(?=\\s+(?:" + trendTokens() + "|" + remarkTokens() + ")\\b|\\s*$)";

		// 시작 지시어 + (공백+토큰)*?  (비탐욕) + 경계 룩어헤드
		// 단, PROB30 TEMPO 같이 지시어 조합이 한 덩어리로 들어오면 fullPattern 쪽에서 이미 처리됨
		return "(?:" + fullPattern() + ")(?:\\s+\\S+)*?" + boundaryLookahead;
	}

	/** 지시어 토큰(다음 세그먼트의 시작점)을 단순 대안식으로 제공합니다. */
	private static String trendTokens() {
		// PROB30/40 뒤 TEMPO 동반 케이스까지 토큰 레벨에서 인지
		return "\\b(?:BECMG|TEMPO|PROB(?:30|40)(?:\\s+TEMPO)?|INTER|NOSIG|FM\\d{4,6}|TL\\d{4}|AT\\d{4})\\b";
	}

	private static String getBecmgRegex() {
		return "(?<becmg>BECMG)";
	}

	private static String getProbRegex() {
		return "(?<prob>PROB(?:30|40)(?:\\s+TEMPO)?)";
	}

	private static String getTempoRegex() {
		return "(?<tempo>TEMPO)";
	}

	private static String getNosigRegex() {
		return "(?<nosig>NOSIG)";
	}

	private static String getInterRegex() {
		return "(?<inter>INTER)";
	}

	private static String getFmRegex() {
		return "(?<fm>FM\\d{4,6})";
	}

	private static String getTlRegex() {
		return "(?<tl>TL\\d{4})";
	}

	private static String getAtRegex() {
		return "(?<at>AT\\d{4})";
	}
}
