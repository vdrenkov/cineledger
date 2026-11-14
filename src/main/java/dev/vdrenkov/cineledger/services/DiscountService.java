package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import dev.vdrenkov.cineledger.mappers.DiscountMapper;
import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.repositories.DiscountRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount addDiscount(DiscountRequest request) {
        if (discountRepository.existsByType(request.type())
                || discountRepository.existsByCode(request.code())) {

            throw new DiscountAlreadyExistsException(
                    ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE);
        }

        return discountRepository.save(
                new Discount(request.type(), request.code(), request.percentage()));
    }

    public List<Discount> getAllDiscounts() {

        return discountRepository.findAll();
    }

    public List<DiscountDto> getAllDiscountDtos() {

        return DiscountMapper.mapDiscountListToDiscountDtoList(getAllDiscounts());
    }

    public Discount getDiscountById(int id) {

        return discountRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new DiscountNotFoundException(
                            ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
                });
    }

    public Discount getDiscountByCode(String code) {

        return discountRepository
                .findByCode(code)
                .orElseThrow(() -> {
                    return new DiscountNotFoundException(
                            ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
                });
    }

    public Discount getDiscountByType(String type) {

        return discountRepository
                .findByType(type)
                .orElseThrow(() -> {
                    return new DiscountNotFoundException(
                            ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
                });
    }

    public DiscountDto getDiscountDtoByType(String type) {

        return DiscountMapper.mapDiscountToDiscountDto(getDiscountByType(type));
    }

    public DiscountDto updateDiscount(DiscountRequest request, int id) {
        final DiscountDto discount = DiscountMapper.mapDiscountToDiscountDto(getDiscountById(id));

        discountRepository.save(
                new Discount(id, request.type(), request.code(), request.percentage()));

        return discount;
    }

    public DiscountDto deleteDiscount(int id) {
        final Discount discount = getDiscountById(id);

        discountRepository.deleteById(id);

        return DiscountMapper.mapDiscountToDiscountDto(discount);
    }

    public int getDiscountPercentage(final String code) {

        if (code == null || code.isBlank()) {
            return 0;
        }

        return discountRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new DiscountNotValidException(
                                ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE))
                .getPercentage();
    }
}
