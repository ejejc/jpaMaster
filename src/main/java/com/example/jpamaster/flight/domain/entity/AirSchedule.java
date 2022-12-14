package com.example.jpamaster.flight.domain.entity;

import com.example.jpamaster.common.domain.BaseEntity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "air_schedule")
@Entity
public class AirSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long airScheduleSeq;

    private LocalDateTime departAt;

    private LocalDateTime arriveAt;

    private Integer flightDistanceKm;

    private Integer estimatedHourSpent;

    private Integer estimatedMinuteSpent;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "airplane_seq")
    private Airplane airplane;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dept_airport_seq")
    private Airport deptAirport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "arr_airport_seq")
    private Airport arrAirport;

    @OneToMany(mappedBy = "airSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AirScheduleSeatType> airScheduleSeatTypes;

    @Builder
    public AirSchedule (LocalDateTime departAt, LocalDateTime arriveAt, Integer flightDistanceKm,
                        Integer estimatedHourSpent, Integer estimatedMinuteSpent, Airplane airplane,
                        Airport deptAirport, Airport arrAirport) {
        this.departAt = departAt;
        this.arriveAt = arriveAt;
        this.flightDistanceKm = flightDistanceKm;
        this.estimatedHourSpent = estimatedHourSpent;
        this.estimatedMinuteSpent = estimatedMinuteSpent;
        this.airplane = airplane;
        this.deptAirport = deptAirport;
        this.arrAirport = arrAirport;
        this.airScheduleSeatTypes = new HashSet<>();
    }
}
