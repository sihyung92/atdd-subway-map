package wooteco.subway.section.domain;

import wooteco.subway.section.exception.SectionHasSameStationsException;
import wooteco.subway.section.exception.SectionNotSequentialException;
import wooteco.subway.station.domain.Station;

public class Section {
    private final Station upStation;
    private final Station downStation;
    private final SectionDistance distance;

    public Section(Station upStation, Station downStation, long distance) {
        this(upStation, downStation, new SectionDistance(distance));
    }

    public Section(Station upStation, Station downStation, SectionDistance distance) {
        checkSameStations(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void checkSameStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SectionHasSameStationsException(String.format("한 개의 역으로 이루어진 노선은 생성할 수 없습니다. 역 이름 : %s", upStation));
        }
    }

    public boolean isExist(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Section mergeWithDownSection(Section next) {
        if (isNotSequential(next)) {
            throw new SectionNotSequentialException(
                    String.format(
                            "이어진 구간이 아닙니다. 앞구간의 하행역 : %s, 뒷 구간의 상행역 : %s",
                            this.downStation,
                            next.upStation
                    )
            );
        }

        return new Section(this.upStation, next.downStation, this.distance.sum(next.distance));
    }

    private boolean isNotSequential(Section next) {
        return !this.downStation.equals(next.upStation);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public long getDistance() {
        return distance.getDistance();
    }
}