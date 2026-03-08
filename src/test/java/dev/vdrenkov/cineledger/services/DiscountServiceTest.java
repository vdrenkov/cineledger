package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.repositories.DiscountRepository;
import dev.vdrenkov.cineledger.testutils.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutils.constants.OrderConstants;
import dev.vdrenkov.cineledger.testutils.factories.DiscountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests discount service behavior.
 */
@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    final DiscountRequest discountRequest = DiscountFactory.getDefaultDiscountRequest();

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    /**
     * Verifies that add Discount no Exceptions success.
     */
    @Test
    void testAddDiscount_noExceptions_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.save(any())).thenReturn(expected);

        final Discount discount = discountService.addDiscount(discountRequest);

        assertEquals(expected, discount);
    }

    /**
     * Verifies that add Discount discount Already Exists throws Discount Already Exists Exception.
     */
    @Test
    void testAddDiscount_discountAlreadyExists_throwsDiscountAlreadyExistsException() {
        when(discountRepository.existsByType(any())).thenReturn(true);

        assertThrows(DiscountAlreadyExistsException.class, () -> discountService.addDiscount(discountRequest));
    }

    /**
     * Verifies that get All Discounts discounts Found success.
     */
    @Test
    void testGetAllDiscounts_discountsFound_success() {
        final List<Discount> expected = DiscountFactory.getDefaultDiscountList();
        when(discountRepository.findAll()).thenReturn(expected);

        final List<Discount> result = discountService.getAllDiscounts();

        assertEquals(expected, result);
    }

    /**
     * Verifies that get All Discounts DTO discounts Found success.
     */
    @Test
    void testGetAllDiscountsDto_discountsFound_success() {
        final List<DiscountDto> expected = DiscountFactory.getDefaultDiscountDtoList();
        when(discountRepository.findAll()).thenReturn(DiscountFactory.getDefaultDiscountList());

        final List<DiscountDto> result = discountService.getAllDiscountDtos();

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Discount By Id discount Found success.
     */
    @Test
    void testGetDiscountById_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountById(DiscountConstants.ID);

        assertEquals(expected, discount);
    }

    /**
     * Verifies that get Discount By Id discount Not Found throws Discount Not Found Exception.
     */
    @Test
    void testGetDiscountById_discountNotFound_throwsDiscountNotFoundException() {
        assertThrows(DiscountNotFoundException.class, () -> discountService.getDiscountById(DiscountConstants.ID));
    }

    /**
     * Verifies that get Discount By Type discount Found success.
     */
    @Test
    void testGetDiscountByType_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByType(anyString())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountByType(DiscountConstants.TYPE);

        assertEquals(expected, discount);
    }

    /**
     * Verifies that get Discount By Type discount Not Found throw Discount Not Found Exception.
     */
    @Test
    void testGetDiscountByType_discountNotFound_throwDiscountNotFoundException() {
        when(discountRepository.findByType(anyString())).thenReturn(Optional.empty());

        assertThrows(DiscountNotFoundException.class, () -> discountService.getDiscountByType(DiscountConstants.TYPE));
    }

    /**
     * Verifies that get Discount By Code discount Found success.
     */
    @Test
    void testGetDiscountByCode_discountFound_success() {
        final Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(expected));

        final Discount discount = discountService.getDiscountByCode(DiscountConstants.CODE);

        assertEquals(expected, discount);
    }

    /**
     * Verifies that get Discount By Code discount Not Found throw Discount Not Found Exception.
     */
    @Test
    void testGetDiscountByCode_discountNotFound_throwDiscountNotFoundException() {
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(DiscountNotFoundException.class, () -> discountService.getDiscountByCode(DiscountConstants.CODE));
    }

    /**
     * Verifies that get Discount DTO By Type discount Found success.
     */
    @Test
    void testGetDiscountDtoByType_discountFound_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findByType(anyString())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

        final DiscountDto discountDto = discountService.getDiscountDtoByType(DiscountConstants.TYPE);

        assertEquals(expected, discountDto);
    }

    /**
     * Verifies that update Discount discount Updated success.
     */
    @Test
    void testUpdateDiscount_discountUpdated_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));
        when(discountRepository.save(any())).thenReturn(DiscountFactory.getDefaultDiscount());

        final DiscountDto discountDto = discountService.updateDiscount(discountRequest, DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    /**
     * Verifies that delete Discount discount Deleted success.
     */
    @Test
    void testDeleteDiscount_discountDeleted_success() {
        final DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

        final DiscountDto discountDto = discountService.deleteDiscount(DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    /**
     * Verifies that apply Discount success.
     */
    @Test
    void testApplyDiscount_success() {
        final double expected = 90;
        final Discount discount = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(discount));

        final double result = discountService.applyDiscount(OrderConstants.TOTAL_PRICE, DiscountConstants.CODE);

        assertEquals(expected, result, 0.0);
    }
}



