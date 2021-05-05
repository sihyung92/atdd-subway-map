package wooteco.subway.line;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LineDao {
    private static Long seq = 0L;
    private static List<Line> lines = new ArrayList<>();

    public static Line save(Line line) {
        Line persistLine = setId(line);
        if (isDuplicatedName(line)) {
            throw new IllegalArgumentException(String.format("노선 이름이 중복되었습니다. 중복된 노선 이름 : %s", line.getName()));
        }
        if (isDuplicatedColor(line)) {
            throw new IllegalArgumentException(String.format("노선 색상이 중복되었습니다. 중복된 노선 색상 : %s", line.getColor()));
        }
        lines.add(persistLine);
        return persistLine;
    }

    private static boolean isDuplicatedColor(Line line) {
        return lines.stream()
                .anyMatch(line1 -> line1.getColor().equals(line.getColor()));
    }

    private static boolean isDuplicatedName(Line line) {
        return lines.stream()
                .anyMatch(line1 -> line1.getName().equals(line.getName()));
    }

    public static List<Line> findAll() {
        return lines;
    }

    public static Line findById(Long id) {
        return lines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("ID에 해당하는 노선이 없습니다. ID : %d", id)));
    }

    private static Line setId(Line line) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, line, ++seq);
        return line;
    }

    public static void delete(Line line) {
        ifAbsent(line);
        lines.remove(line);
    }

    public static void deleteAll() {
        seq = 0L;
        lines = new ArrayList<>();
    }

    private static void ifAbsent(Line line) {
        if (!lines.contains(line)) {
            throw new IllegalArgumentException("노선이 존재하지 않습니다.");
        }
    }
}