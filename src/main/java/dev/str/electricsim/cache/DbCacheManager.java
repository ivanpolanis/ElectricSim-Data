package dev.str.electricsim.cache;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public abstract class DbCacheManager<T, R extends JpaRepository<T, ?>> {
    final R repository;

    public DbCacheManager(R repository) {
        this.repository = repository;
    }

    public abstract List<T> fetchFromApi(LocalDate date);

    public abstract List<T> getOrFetch(LocalDate date);
}
