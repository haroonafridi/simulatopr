package com.hkcapital.portoflio.repository.mocks;

import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.repository.OrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderRepositoryMock implements OrderRepository
{
    @Override
    public void flush()
    {

    }

    @Override
    public <S extends EtoroOrder> S saveAndFlush(S entity)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> List<S> saveAllAndFlush(Iterable<S> entities)
    {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<EtoroOrder> entities)
    {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers)
    {

    }

    @Override
    public void deleteAllInBatch()
    {

    }

    /**
     * @param integer
     * @deprecated
     */
    @Override
    public EtoroOrder getOne(Integer integer)
    {
        return null;
    }

    /**
     * @param integer
     * @deprecated
     */
    @Override
    public EtoroOrder getById(Integer integer)
    {
        return null;
    }

    @Override
    public EtoroOrder getReferenceById(Integer integer)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> Optional<S> findOne(Example<S> example)
    {
        return Optional.empty();
    }

    @Override
    public <S extends EtoroOrder> List<S> findAll(Example<S> example)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> List<S> findAll(Example<S> example, Sort sort)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> Page<S> findAll(Example<S> example, Pageable pageable)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> long count(Example<S> example)
    {
        return 0;
    }

    @Override
    public <S extends EtoroOrder> boolean exists(Example<S> example)
    {
        return false;
    }

    @Override
    public <S extends EtoroOrder, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> S save(S entity)
    {
        return null;
    }

    @Override
    public <S extends EtoroOrder> List<S> saveAll(Iterable<S> entities)
    {
        return null;
    }

    @Override
    public Optional<EtoroOrder> findById(Integer integer)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer)
    {
        return false;
    }

    @Override
    public List<EtoroOrder> findAll()
    {
        return null;
    }

    @Override
    public List<EtoroOrder> findAllById(Iterable<Integer> integers)
    {
        return null;
    }

    @Override
    public long count()
    {
        return 0;
    }

    @Override
    public void deleteById(Integer integer)
    {

    }

    @Override
    public void delete(EtoroOrder entity)
    {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers)
    {

    }

    @Override
    public void deleteAll(Iterable<? extends EtoroOrder> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }

    @Override
    public List<EtoroOrder> findAll(Sort sort)
    {
        return null;
    }

    @Override
    public Page<EtoroOrder> findAll(Pageable pageable)
    {
        return null;
    }
}
