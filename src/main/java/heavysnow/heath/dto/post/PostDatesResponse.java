package heavysnow.heath.dto.post;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDatesResponse {

    private List<LocalDate> postDates;

    public static PostDatesResponse of(List<LocalDateTime> postDatetimes) {

        return new PostDatesResponse(postDatetimes.stream()
                .map(LocalDate::from)
                .distinct()
                .collect(Collectors.toList()));
    }
}
