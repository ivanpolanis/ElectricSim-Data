package dev.str.electricsim.cache;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public abstract class DbCacheManager<E, T, R extends JpaRepository<E, ?>> {
    final R repository;

    public DbCacheManager(R repository) {
        this.repository = repository;
    }

    public abstract T fetchFromApi(LocalDate date);

    public abstract T getOrFetch(LocalDate date);
}
