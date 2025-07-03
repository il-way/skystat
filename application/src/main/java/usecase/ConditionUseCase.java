package usecase;

public interface ConditionUseCase<T> {

	Boolean execute(T query);

}
