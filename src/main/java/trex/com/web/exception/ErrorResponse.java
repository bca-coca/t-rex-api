package trex.com.web.exception;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ErrorResponse(
		int status,
		@NonNull String error,
		@NonNull String message,
		@NonNull String path,
		List< String > details,
		LocalDate timestamp
) {
	public ErrorResponse {
		details = details != null ? List.copyOf(details) : List.of();
		timestamp = timestamp != null ? timestamp : LocalDate.now();
	}
}