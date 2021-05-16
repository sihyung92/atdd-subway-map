package wooteco.subway.section.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.section.exception.SectionDistanceTooShortException;
import wooteco.subway.section.exception.SectionNotSequentialException;
import wooteco.subway.section.exception.SectionsHasDuplicateException;
import wooteco.subway.section.exception.SectionsHasNotSectionException;
import wooteco.subway.section.exception.SectionsIllegalArgumentException;
import wooteco.subway.section.exception.SectionsSizeTooSmallException;
import wooteco.subway.station.fixture.StationFixture;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.section.domain.Fixture.*;

@DisplayName("구간 일급 컬렉션 기능 테스트")
class OrderedSectionsTest {
    Fixture sectionsFixture;

    @BeforeEach
    void setUp() {
        sectionsFixture = new Fixture();
    }

    @DisplayName("노선구간의 생성")
    @Test
    void createSections() {
        //given
        //when
        //then
        assertThat(new OrderedSections(FIRST_SECTION, SECOND_SECTION, THIRD_SECTION))
                .isNotNull();
    }

    @DisplayName("노선구간의 종점이 1개가 아니면 예외")
    @Test
    void whenEndStationsSizeNotOne() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new OrderedSections(FIRST_SECTION, DOUBLE_END_UPSTATION_SECTION))
                .isInstanceOf(SectionsIllegalArgumentException.class);
    }

    @DisplayName("노선 구간을 생성시 상행종점역 -> 하행종점역 방면으로 정렬되는지 확인")
    @Test
    void sortSectionsTest() {
        //given
        //when
        OrderedSections orderedSections = new OrderedSections(SECOND_SECTION, FIRST_SECTION, FOURTH_SECTION, THIRD_SECTION);
        //then
        assertThat(orderedSections.getSections()).containsExactly(FIRST_SECTION, SECOND_SECTION, THIRD_SECTION, FOURTH_SECTION);
    }

    @DisplayName("역정렬 조회 기능 테스트")
    @Test
    void getReverseTest() {
        //given
        //when
        OrderedSections orderedSections = new OrderedSections(SECOND_SECTION, FIRST_SECTION, FOURTH_SECTION, THIRD_SECTION);
        //then
        assertThat(orderedSections.getReverseSections()).containsExactly(FOURTH_SECTION, THIRD_SECTION, SECOND_SECTION, FIRST_SECTION);
    }

    @DisplayName("역을 추가하는 기능 테스트")
    @ParameterizedTest
    @MethodSource
    void addSectionTest(Section newSection, OrderedSections expectedResult) {
        //given
        //when
        //then
        assertThat(getFirstToFifthSections().addSection(newSection).getSections())
                .containsAll(expectedResult.getSections());
    }

    static Stream<Arguments> addSectionTest() {
        return Stream.of(
                Arguments.of(BEFORE_FIRST_SECTION, BEFORE_FIRST_SECTION_RESULT),
                Arguments.of(FIFTH_SECTION, FIFTH_SECTION_RESULT),
                Arguments.of(BETWEEN_THIRD_AND_FOURTH_SECTION, BETWEEN_THIRD_AND_FOURTH_RESULT),
                Arguments.of(BETWEEN_SECOND_AND_FIRST_SECTION, BETWEEN_SECOND_AND_FIRST_RESULT)
        );
    }

    @DisplayName("연결된 구간이 없는 구간을 추가하면 예외")
    @Test
    void whenNotSequentialSectionAddTest() {
        //given
        Section notSequentialSection = new Section(StationFixture.SAMSUNG_STATION, StationFixture.GYODAE_STATION, 10);
        //when
        //then
        assertThatThrownBy(() -> getFirstToFifthSections().addSection(notSequentialSection))
                .isInstanceOf(SectionNotSequentialException.class);
    }

    @DisplayName("상행역과 하행역이 이미 모두 등록되어있는 구간에 추가하면 예외")
    @Test
    void whenUpAndDownAlreadyAddedTest() {
        //given
        //when
        //then
        assertThatThrownBy(() -> getFirstToFifthSections().addSection(FIRST_SECTION))
                .isInstanceOf(SectionsHasDuplicateException.class);
    }

    @DisplayName("동일한 노선이 있는경우 예외")
    @Test
    void whenDuplicateSectionTest() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new OrderedSections(FIRST_SECTION, FIRST_SECTION, SECOND_SECTION))
                .isInstanceOf(SectionsHasDuplicateException.class);
    }

    @DisplayName("구간 삭제 테스트")
    @Test
    void removeSectionTest() {
        //given
        OrderedSections sections = getFirstToFifthSections();
        //when
        OrderedSections newSections = sections.removeSection(StationFixture.JAMSIL_STATION);
        //then
        assertThat(newSections.getSections()).containsExactly(FIRST_AND_SECOND_MERGED_SECTION, THIRD_SECTION, FOURTH_SECTION);
    }

    @DisplayName("크기가 1인 구간에서 구간을 삭제하려고 하면 예외")
    @Test
    void whenOnlyOneSectionsRemoveTest() {
        //given
        OrderedSections onlyOneSection = new OrderedSections(new Section(StationFixture.GANGNAM_STATION, StationFixture.JAMSIL_STATION, 10));
        //when
        //then
        assertThatThrownBy(() -> onlyOneSection.removeSection(StationFixture.GANGNAM_STATION))
                .isInstanceOf(SectionsSizeTooSmallException.class);
    }

    @DisplayName("새로운 구간의 길이가 기존의 구간길이보다 크거나 같으면 예외")
    @Test
    void whenNewSectionTooLong() {
        //given
        //when
        //then
        assertThatThrownBy(() -> getFirstToFifthSections().addSection(TOO_LONG_SECTION))
                .isInstanceOf(SectionDistanceTooShortException.class);
    }

    @DisplayName("노선을 삭제하려할 때 등록되어있지 않은 노선이면 예외")
    @Test
    void whenSectionsHasNotSection() {
        //given
        //when
        //then
        assertThatThrownBy(() -> getFirstToFifthSections().removeSection(StationFixture.YEOKSAM_STATION))
                .isInstanceOf(SectionsHasNotSectionException.class);
    }

    private OrderedSections getFirstToFifthSections() {
        return sectionsFixture.getFirstToFourthSections();
    }
}