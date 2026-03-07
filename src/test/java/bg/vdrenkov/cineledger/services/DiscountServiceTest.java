package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import bg.vdrenkov.cineledger.mappers.DiscountMapper;
import bg.vdrenkov.cineledger.models.dtos.DiscountDto;
import bg.vdrenkov.cineledger.models.entities.Discount;
import bg.vdrenkov.cineledger.repositories.DiscountRepository;
import bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants;
import bg.vdrenkov.cineledger.testUtils.constants.OrderConstants;
import bg.vdrenkov.cineledger.testUtils.factories.DiscountFactory;
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

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountMapper discountMapper;

    @InjectMocks
    private DiscountService discountService;

    @Test
    public void testAddDiscount_noExceptions_success() {
        Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.save(any())).thenReturn(expected);

        Discount discount = discountService.addDiscount(DiscountFactory.getDefaultDiscountRequest());

        assertEquals(expected, discount);
    }

    @Test
    public void testAddDiscount_discountAlreadyExists_throwsDiscountAlreadyExistsException() {
        assertThrows(DiscountAlreadyExistsException.class, () -> {

            when(discountRepository.existsByType(any())).thenReturn(true);

            discountService.addDiscount(DiscountFactory.getDefaultDiscountRequest());

        });
    }

    @Test
    public void testGetAllDiscounts_discountsFound_success() {
        List<Discount> expected = DiscountFactory.getDefaultDiscountList();
        when(discountRepository.findAll()).thenReturn(expected);

        List<Discount> result = discountService.getAllDiscounts();

        assertEquals(expected, result);
    }

    @Test
    public void testGetAllDiscountsDto_discountsFound_success() {
        List<DiscountDto> expected = DiscountFactory.getDefaultDiscountDtoList();
        when(discountRepository.findAll()).thenReturn(DiscountFactory.getDefaultDiscountList());
        when(discountMapper.mapDiscountListToDiscountDtoList(any())).thenReturn(expected);

        List<DiscountDto> result = discountService.getAllDiscountDtos();

        assertEquals(expected, result);
    }

    @Test
    public void testGetDiscountById_discountFound_success() {
        Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Discount discount = discountService.getDiscountById(DiscountConstants.ID);

        assertEquals(expected, discount);
    }

    @Test
    public void testGetDiscountById_discountNotFound_throwsDiscountNotFoundException() {
        assertThrows(DiscountNotFoundException.class, () -> {

            discountService.getDiscountById(DiscountConstants.ID);

        });
    }

    @Test
    public void testGetDiscountByType_discountFound_success() {
        Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByType(anyString())).thenReturn(Optional.of(expected));

        Discount discount = discountService.getDiscountByType(DiscountConstants.TYPE);

        assertEquals(expected, discount);
    }

    @Test
    public void testGetDiscountByType_discountNotFound_throwDiscountNotFoundException() {
        assertThrows(DiscountNotFoundException.class, () -> {

            when(discountRepository.findByType(anyString())).thenReturn(Optional.empty());

            discountService.getDiscountByType(DiscountConstants.TYPE);

        });
    }

    @Test
    public void testGetDiscountByCode_discountFound_success() {
        Discount expected = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(expected));

        Discount discount = discountService.getDiscountByCode(DiscountConstants.CODE);

        assertEquals(expected, discount);
    }

    @Test
    public void testGetDiscountByCode_discountNotFound_throwDiscountNotFoundException() {
        assertThrows(DiscountNotFoundException.class, () -> {

            when(discountRepository.findByCode(anyString())).thenReturn(Optional.empty());

            discountService.getDiscountByCode(DiscountConstants.CODE);

        });
    }

    @Test
    public void testGetDiscountDtoByType_discountFound_success() {
        DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountRepository.findByType(anyString())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));
        when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);

        DiscountDto discountDto = discountService.getDiscountDtoByType(DiscountConstants.TYPE);

        assertEquals(expected, discountDto);
    }

    @Test
    public void testUpdateDiscount_discountUpdated_success() {
        DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(new Discount()));
        when(discountRepository.save(any())).thenReturn(DiscountFactory.getDefaultDiscount());

        DiscountDto discountDto = discountService.updateDiscount(DiscountFactory.getDefaultDiscountRequest(),
            DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    @Test
    public void testDeleteDiscount_discountDeleted_success() {
        DiscountDto expected = DiscountFactory.getDefaultDiscountDto();
        when(discountMapper.mapDiscountToDiscountDto(any())).thenReturn(expected);
        when(discountRepository.findById(anyInt())).thenReturn(Optional.of(DiscountFactory.getDefaultDiscount()));

        DiscountDto discountDto = discountService.deleteDiscount(DiscountConstants.ID);

        assertEquals(expected, discountDto);
    }

    @Test
    public void testApplyDiscount_success() {
        double expected = 90;
        Discount discount = DiscountFactory.getDefaultDiscount();
        when(discountRepository.findByCode(anyString())).thenReturn(Optional.of(discount));

        double result = discountService.applyDiscount(OrderConstants.TOTAL_PRICE, DiscountConstants.CODE);

        assertEquals(expected, result, 0.0);
    }
}



