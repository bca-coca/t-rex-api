	package trex.com.web.exception;

	import jakarta.servlet.http.HttpServletRequest;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.ExceptionHandler;
	import org.springframework.web.bind.annotation.RestControllerAdvice;

	import lombok.extern.slf4j.Slf4j;

	import java.time.LocalDate;
	import java.util.List;

	@Slf4j
	@RestControllerAdvice
	public class GlobalExceptionHandler {

		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<ErrorResponse> handleResourceNotFoundException( ResourceNotFoundException ex, HttpServletRequest request) {
			ErrorResponse error = ErrorResponse.builder()
					.status(HttpStatus.NOT_FOUND.value())
					.error(HttpStatus.NOT_FOUND.getReasonPhrase())
					.message(ex.getMessage())
					.path(request.getRequestURI())
					.details(List.of(ex.getResourceName(), ex.getFieldName(), ex.getFieldValue().toString()))
					.timestamp(LocalDate.now())
					.build();
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}

		@ExceptionHandler(RuntimeException.class)
		public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
			ErrorResponse error = ErrorResponse.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
					.message(ex.getMessage())
					.path(request.getRequestURI())
					.details(List.of("An unexpected error occurred"))
					.timestamp(LocalDate.now())
					.build();
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
			ErrorResponse error = ErrorResponse.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
					.message(ex.getMessage())
					.path(request.getRequestURI())
					.details(List.of("An unexpected error occurred"))
					.timestamp(LocalDate.now())
					.build();
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}