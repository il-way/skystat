package usecase;

public interface ConditionUseCase<T> {

	boolean execute(T query);

}
