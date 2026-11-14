package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.repositories.DiscountRepository;
import dev.vdrenkov.cineledger.testutils.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutils.factories.DiscountFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    final DiscountRequest discountRequest = DiscountFactory.getDefaultDiscountRequest();

    @Mock private DiscountRepository discountRepository;

    @InjectMocks private DiscountService discountService;

    @Test
    void testAddDiscount_noExceptions_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.save(any())).thenReturn(expected);

        final Discount discount = discountService.addDiscount(discountRequest);

        assertEquals(expected, discount);
    }

    @Test
    void testAddDiscount_discountAlreadyExists_throwsDiscountAlreadyExistsException() {
        when(discountRepository.existsByType(any())).thenReturn(true);

        assertThrows(
                DiscountAlreadyExistsException.class,
                () -> discountService.addDiscount(discountRequest));
    }

    @Test
    void testGetAllDiscounts_discountsFound_success() {
        final List<Discount> expected = DiscountFactory.getDefaultDiscountList();
        when(discountRepository.findAll()).thenReturn(expected);

        final List<Discount> result = discountService.getAllDiscounts();

        assertEquals(expected, result);
    }

    @Test
    void testGetAllDiscountsDto_discountsFound_success() {
        final List<DiscountDto> expected = DiscountFactory.getDefaultDiscountDtoList();
        when(discountRepository.findAll()).thenReturn(DiscountFactory.getDefaultDiscountList());

        final List<DiscountDto> result = discountService.getAllDiscountDtos();

        assertEquals(expected, result);
    }

    @Test
    void testGetDiscountById_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountById(DiscountConstants.ID);

        assertEquals(expected, discount);
    }

    @Test
    void testGetDiscountById_discountNotFound_throwsDiscountNotFoundException() {
        assertThrows(
                DiscountNotFoundException.class,
                () -> discountService.getDiscountById(DiscountConstants.ID));
    }

    @Test
    void testGetDiscountByType_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByType(anyString())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountByType(DiscountConstants.TYPE);

        assertEquals(expected, discount);
    }

    @Test
    void testGetDiscountByType_discountNotFound_throwDiscountNotFoundException() {
        when(discountRepository.findByType(anyString())).thenReturn(Optional.empty());

        assertThrows(
                DiscountNotFoundException.class,
                () -> discountService.getDiscountByType(DiscountConstants.TYPE));
    }

    @Test
    void testGetDiscountByCode_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountByCode(DiscountConstants.CODE);

        assertEquals(expected, discount);
    }

    @Test
    void testGetDiscountByCode_discountNotFound_throwDiscountNotFoundException() {
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(
                DiscountNotFoundException.class,
                () -> discountService.getDiscountByCode(DiscountConstants.CODE));
    }

    @Test
    void testGetDiscountDtoByType_discountFound_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findByType(anyString()))
                .thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

        final DiscountDto discountDto =
                discountService.getDiscountDtoByType(DiscountConstants.TYPE);

        assertEquals(expected, discountDto);
    }

    @Test
    void testUpdateDiscount_discountUpdated_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findById(anyInt()))
                .thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));
        when(discountRepository.save(any())).thenReturn(DiscountFactory.getDefaultDiscount());

        final DiscountDto discountDto =
                discountService.updateDiscount(discountRequest, DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    @Test
    void testDeleteDiscount_discountDeleted_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findById(anyInt()))
                .thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

        final DiscountDto discountDto = discountService.deleteDiscount(DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    @Test
    void testApplyDiscount_success() {
        final int expected = 10;
        final Discount discount = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(discount));

        final int result = discountService.getDiscountPercentage(DiscountConstants.CODE);

        assertEquals(expected, result);
    }

    @Test
    void testApplyDiscount_invalidCode_throwsDiscountNotValidException() {
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.empty());

        final DiscountNotValidException exception =
                assertThrows(
                        DiscountNotValidException.class,
                        () -> discountService.getDiscountPercentage(DiscountConstants.CODE));

        assertEquals(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE, exception.getMessage());
    }
}
