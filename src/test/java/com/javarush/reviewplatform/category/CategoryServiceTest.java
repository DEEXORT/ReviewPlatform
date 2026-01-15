package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.common.AbstractBaseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Unit Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryTo testCategoryTo;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic devices")
                .build();

        testCategoryTo = CategoryTo.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic devices")
                .build();
    }

    // ========== ТЕСТЫ КОНСТРУКТОРА ==========

    @Test
    @DisplayName("Конструктор: должен создавать сервис с валидными зависимостями")
    void constructor_ShouldCreateServiceWithValidDependencies() {
        // Act
        CategoryService service = new CategoryService(categoryRepository, categoryMapper);

        // Assert
        assertNotNull(service);
        assertTrue(service instanceof AbstractBaseService);
    }

    // ========== ТЕСТЫ МЕТОДА save() ==========

    @Nested
    @DisplayName("Метод save()")
    class SaveMethodTests {

        @Test
        @DisplayName("Должен сохранить категорию и вернуть DTO")
        void save_ShouldSaveCategoryAndReturnDto() {
            // Arrange
            when(categoryMapper.mapToEntity(testCategoryTo)).thenReturn(testCategory);
            when(categoryRepository.save(testCategory)).thenReturn(testCategory);

            // Act
            CategoryTo result = categoryService.save(testCategoryTo);

            // Assert
            assertNotNull(result);
            assertSame(testCategoryTo, result); // Возвращается тот же объект
            verify(categoryMapper).mapToEntity(testCategoryTo);
            verify(categoryRepository).save(testCategory);
        }

        @Test
        @DisplayName("Должен обработать новую категорию без ID")
        void save_ShouldHandleNewCategoryWithoutId() {
            // Arrange
            CategoryTo newCategoryTo = CategoryTo.builder()
                    .id(null)
                    .name("New Category")
                    .description("Description")
                    .build();

            Category newCategory = Category.builder()
                    .id(null)
                    .name("New Category")
                    .description("Description")
                    .build();

            when(categoryMapper.mapToEntity(newCategoryTo)).thenReturn(newCategory);
            when(categoryRepository.save(newCategory)).thenReturn(newCategory);

            // Act
            CategoryTo result = categoryService.save(newCategoryTo);

            // Assert
            assertNotNull(result);
            assertSame(newCategoryTo, result);
            verify(categoryRepository).save(newCategory);
        }

        @Test
        @DisplayName("Должен обработать категорию без описания")
        void save_ShouldHandleCategoryWithoutDescription() {
            // Arrange
            CategoryTo categoryToWithoutDesc = CategoryTo.builder()
                    .id(2L)
                    .name("Books")
                    .description(null)
                    .build();

            Category categoryWithoutDesc = Category.builder()
                    .id(2L)
                    .name("Books")
                    .description(null)
                    .build();

            when(categoryMapper.mapToEntity(categoryToWithoutDesc)).thenReturn(categoryWithoutDesc);
            when(categoryRepository.save(categoryWithoutDesc)).thenReturn(categoryWithoutDesc);

            // Act
            CategoryTo result = categoryService.save(categoryToWithoutDesc);

            // Assert
            assertNotNull(result);
            assertSame(categoryToWithoutDesc, result);
        }

        @Test
        @DisplayName("Должен пробросить исключение при ошибке маппера")
        void save_ShouldPropagateException_WhenMapperFails() {
            // Arrange
            when(categoryMapper.mapToEntity(any(CategoryTo.class)))
                    .thenThrow(new RuntimeException("Mapping error"));

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> categoryService.save(testCategoryTo));
        }

        @Test
        @DisplayName("Должен пробросить исключение при ошибке репозитория")
        void save_ShouldPropagateException_WhenRepositoryFails() {
            // Arrange
            when(categoryMapper.mapToEntity(testCategoryTo)).thenReturn(testCategory);
            when(categoryRepository.save(testCategory))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> categoryService.save(testCategoryTo));
        }

        @Test
        @DisplayName("Должен выбрасывать NullPointerException при null входных данных")
        void save_ShouldThrowNullPointerException_WhenInputIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class,
                    () -> categoryService.save(null));
        }
    }

    // ========== ТЕСТЫ МЕТОДА getById() ==========

    @Nested
    @DisplayName("Метод getById()")
    class GetByIdMethodTests {

        @Test
        @DisplayName("Должен вернуть DTO категории при существующем ID")
        void getById_ShouldReturnCategoryDto_WhenCategoryExists() {
            // Arrange
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
            when(categoryMapper.mapToDto(testCategory)).thenReturn(testCategoryTo);

            // Act
            CategoryTo result = categoryService.getById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Electronics", result.getName());
            assertEquals("Electronic devices", result.getDescription());
            verify(categoryRepository).findById(1L);
            verify(categoryMapper).mapToDto(testCategory);
        }

        @Test
        @DisplayName("Должен выбрасывать EntityNotFoundException при несуществующем ID")
        void getById_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
            // Arrange
            Long nonExistentId = 999L;
            when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> categoryService.getById(nonExistentId)
            );

            assertEquals("Entity with id = 999 not found", exception.getMessage());
            verify(categoryRepository).findById(nonExistentId);
            verify(categoryMapper, never()).mapToDto(any());
        }

        @Test
        @DisplayName("Должен обработать ID = 0")
        void getById_ShouldHandleZeroId() {
            // Arrange
            Category categoryWithZeroId = Category.builder()
                    .id(0L)
                    .name("Test")
                    .build();

            CategoryTo categoryToWithZeroId = CategoryTo.builder()
                    .id(0L)
                    .name("Test")
                    .build();

            when(categoryRepository.findById(0L)).thenReturn(Optional.of(categoryWithZeroId));
            when(categoryMapper.mapToDto(categoryWithZeroId)).thenReturn(categoryToWithZeroId);

            // Act
            CategoryTo result = categoryService.getById(0L);

            // Assert
            assertNotNull(result);
            assertEquals(0L, result.getId());
        }

        @Test
        @DisplayName("Должен обработать отрицательный ID")
        void getById_ShouldHandleNegativeId() {
            // Arrange
            Long negativeId = -1L;
            when(categoryRepository.findById(negativeId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> categoryService.getById(negativeId));
        }

        @Test
        @DisplayName("Должен выбрасывать NullPointerException при null ID")
        void getById_ShouldThrowNullPointerException_WhenIdIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class,
                    () -> categoryService.getById(null));
        }

        @Test
        @DisplayName("Должен обработать ситуацию когда маппер возвращает null")
        void getById_ShouldHandleMapperReturningNull() {
            // Arrange
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
            when(categoryMapper.mapToDto(testCategory)).thenReturn(null);

            // Act
            CategoryTo result = categoryService.getById(1L);

            // Assert
            assertNull(result);
        }
    }

    // ========== ТЕСТЫ МЕТОДА getAll() ==========

    @Nested
    @DisplayName("Метод getAll()")
    class GetAllMethodTests {

        @Test
        @DisplayName("Должен вернуть список всех категорий")
        void getAll_ShouldReturnListOfAllCategories() {
            // Arrange
            Category category2 = Category.builder()
                    .id(2L)
                    .name("Books")
                    .description("Books and literature")
                    .build();

            CategoryTo categoryTo2 = CategoryTo.builder()
                    .id(2L)
                    .name("Books")
                    .description("Books and literature")
                    .build();

            List<Category> categories = Arrays.asList(testCategory, category2);
            List<CategoryTo> categoryTos = Arrays.asList(testCategoryTo, categoryTo2);

            when(categoryRepository.findAll()).thenReturn(categories);
            when(categoryMapper.mapToDtoList(categories)).thenReturn(categoryTos);

            // Act
            List<CategoryTo> result = categoryService.getAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Electronics", result.get(0).getName());
            assertEquals("Books", result.get(1).getName());
            verify(categoryRepository).findAll();
            verify(categoryMapper).mapToDtoList(categories);
        }

        @Test
        @DisplayName("Должен вернуть пустой список когда нет категорий")
        void getAll_ShouldReturnEmptyList_WhenNoCategories() {
            // Arrange
            List<Category> emptyList = List.of();
            List<CategoryTo> emptyDtoList = List.of();

            when(categoryRepository.findAll()).thenReturn(emptyList);
            when(categoryMapper.mapToDtoList(emptyList)).thenReturn(emptyDtoList);

            // Act
            List<CategoryTo> result = categoryService.getAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Должен обработать одну категорию в списке")
        void getAll_ShouldHandleSingleCategory() {
            // Arrange
            List<Category> singleList = List.of(testCategory);
            List<CategoryTo> singleDtoList = List.of(testCategoryTo);

            when(categoryRepository.findAll()).thenReturn(singleList);
            when(categoryMapper.mapToDtoList(singleList)).thenReturn(singleDtoList);

            // Act
            List<CategoryTo> result = categoryService.getAll();

            // Assert
            assertEquals(1, result.size());
            assertEquals(testCategoryTo, result.get(0));
        }

        @Test
        @DisplayName("Должен обработать ситуацию когда маппер возвращает null")
        void getAll_ShouldHandleMapperReturningNull() {
            // Arrange
            List<Category> categories = List.of(testCategory);

            when(categoryRepository.findAll()).thenReturn(categories);
            when(categoryMapper.mapToDtoList(categories)).thenReturn(null);

            // Act
            List<CategoryTo> result = categoryService.getAll();

            // Assert
            assertNull(result);
        }

        @Test
        @DisplayName("Должен корректно работать с большим количеством категорий")
        void getAll_ShouldHandleLargeNumberOfCategories() {
            // Arrange
            List<Category> largeList = createLargeCategoryList(50);
            List<CategoryTo> largeDtoList = createLargeCategoryToList(50);

            when(categoryRepository.findAll()).thenReturn(largeList);
            when(categoryMapper.mapToDtoList(largeList)).thenReturn(largeDtoList);

            // Act
            List<CategoryTo> result = categoryService.getAll();

            // Assert
            assertEquals(50, result.size());
        }

        private List<Category> createLargeCategoryList(int count) {
            return java.util.stream.IntStream.range(0, count)
                    .mapToObj(i -> Category.builder()
                            .id((long) i)
                            .name("Category " + i)
                            .description("Description " + i)
                            .build())
                    .toList();
        }

        private List<CategoryTo> createLargeCategoryToList(int count) {
            return java.util.stream.IntStream.range(0, count)
                    .mapToObj(i -> CategoryTo.builder()
                            .id((long) i)
                            .name("Category " + i)
                            .description("Description " + i)
                            .build())
                    .collect(Collectors.toList());
        }
    }

    // ========== ТЕСТЫ МЕТОДА deleteById() ==========

    @Nested
    @DisplayName("Метод deleteById()")
    class DeleteByIdMethodTests {

        @Test
        @DisplayName("Должен вернуть true и удалить категорию при существующем ID")
        void deleteById_ShouldReturnTrueAndDelete_WhenCategoryExists() {
            // Arrange
            when(categoryRepository.existsById(1L)).thenReturn(true);

            // Act
            boolean result = categoryService.deleteById(1L);

            // Assert
            assertTrue(result);
            verify(categoryRepository).existsById(1L);
            verify(categoryRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Должен вернуть false при несуществующем ID")
        void deleteById_ShouldReturnFalse_WhenCategoryDoesNotExist() {
            // Arrange
            Long nonExistentId = 999L;
            when(categoryRepository.existsById(nonExistentId)).thenReturn(false);

            // Act
            boolean result = categoryService.deleteById(nonExistentId);

            // Assert
            assertFalse(result);
            verify(categoryRepository).existsById(nonExistentId);
            verify(categoryRepository, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("Должен вернуть false при null ID")
        void deleteById_ShouldReturnFalse_WhenIdIsNull() {
            // Act
            boolean result = categoryService.deleteById(null);

            // Assert
            assertFalse(result);
            verify(categoryRepository, never()).existsById(any(Long.class));
            verify(categoryRepository, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("Должен обработать ID = 0")
        void deleteById_ShouldHandleZeroId() {
            // Arrange
            when(categoryRepository.existsById(0L)).thenReturn(false);

            // Act
            boolean result = categoryService.deleteById(0L);

            // Assert
            assertFalse(result);
            verify(categoryRepository).existsById(0L);
        }

        @Test
        @DisplayName("Должен пробросить исключение при ошибке проверки существования")
        void deleteById_ShouldPropagateException_WhenExistsCheckFails() {
            // Arrange
            when(categoryRepository.existsById(1L))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> categoryService.deleteById(1L));
        }

        @Test
        @DisplayName("Должен пробросить исключение при ошибке удаления")
        void deleteById_ShouldPropagateException_WhenDeleteFails() {
            // Arrange
            when(categoryRepository.existsById(1L)).thenReturn(true);
            doThrow(new RuntimeException("Delete failed"))
                    .when(categoryRepository).deleteById(1L);

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> categoryService.deleteById(1L));
        }
    }

    // ========== ИНТЕГРАЦИОННЫЕ ТЕСТЫ ==========

    @Nested
    @DisplayName("Интеграционные сценарии")
    class IntegrationScenariosTests {

        @Test
        @DisplayName("Должен корректно выполнять полный цикл CRUD операций")
        void shouldPerformFullCrudCycle() {
            // 1. Save - Create
            CategoryTo newCategoryTo = CategoryTo.builder()
                    .id(null)
                    .name("New Category")
                    .description("New Description")
                    .build();

            Category newCategory = Category.builder()
                    .id(null)
                    .name("New Category")
                    .description("New Description")
                    .build();

            Category savedCategory = Category.builder()
                    .id(100L)
                    .name("New Category")
                    .description("New Description")
                    .build();

            when(categoryMapper.mapToEntity(newCategoryTo)).thenReturn(newCategory);
            when(categoryRepository.save(newCategory)).thenReturn(savedCategory);

            CategoryTo savedTo = categoryService.save(newCategoryTo);
            assertNotNull(savedTo);

            // 2. Get - Read
            when(categoryRepository.findById(100L)).thenReturn(Optional.of(savedCategory));
            when(categoryMapper.mapToDto(savedCategory)).thenReturn(
                    CategoryTo.builder()
                            .id(100L)
                            .name("New Category")
                            .description("New Description")
                            .build()
            );

            CategoryTo retrieved = categoryService.getById(100L);
            assertNotNull(retrieved);
            assertEquals("New Category", retrieved.getName());

            // 3. GetAll - Read All
            List<Category> allCategories = List.of(testCategory, savedCategory);
            List<CategoryTo> allCategoryTos = List.of(
                    testCategoryTo,
                    CategoryTo.builder().id(100L).name("New Category").build()
            );

            when(categoryRepository.findAll()).thenReturn(allCategories);
            when(categoryMapper.mapToDtoList(allCategories)).thenReturn(allCategoryTos);

            List<CategoryTo> all = categoryService.getAll();
            assertEquals(2, all.size());

            // 4. Delete
            when(categoryRepository.existsById(100L)).thenReturn(true);

            boolean deleted = categoryService.deleteById(100L);
            assertTrue(deleted);

            // Проверяем что все методы были вызваны нужное количество раз
            verify(categoryRepository, times(1)).save(any(Category.class));
            verify(categoryRepository, times(1)).findById(100L);
            verify(categoryRepository, times(1)).findAll();
            verify(categoryRepository, times(1)).deleteById(100L);
        }

        @Test
        @DisplayName("Должен обрабатывать категорию с максимальной длиной полей")
        void shouldHandleCategoryWithMaximumFieldLengths() {
            // Arrange
            String maxName = "A".repeat(255); // Максимальная длина для уникального поля
            String longDescription = "B".repeat(1000);

            Category categoryWithMaxFields = Category.builder()
                    .id(10L)
                    .name(maxName)
                    .description(longDescription)
                    .build();

            CategoryTo categoryToWithMaxFields = CategoryTo.builder()
                    .id(10L)
                    .name(maxName)
                    .description(longDescription)
                    .build();

            when(categoryRepository.findById(10L)).thenReturn(Optional.of(categoryWithMaxFields));
            when(categoryMapper.mapToDto(categoryWithMaxFields)).thenReturn(categoryToWithMaxFields);

            // Act
            CategoryTo result = categoryService.getById(10L);

            // Assert
            assertNotNull(result);
            assertEquals(maxName, result.getName());
            assertEquals(longDescription, result.getDescription());
        }

        @Test
        @DisplayName("Должен корректно работать с категорией содержащей спецсимволы")
        void shouldHandleCategoryWithSpecialCharacters() {
            // Arrange
            String nameWithSpecials = "Café & Restaurant 🍕";
            String descWithSpecials = "Описание с кириллицей и эмодзи 😊";

            Category specialCategory = Category.builder()
                    .id(20L)
                    .name(nameWithSpecials)
                    .description(descWithSpecials)
                    .build();

            CategoryTo specialCategoryTo = CategoryTo.builder()
                    .id(20L)
                    .name(nameWithSpecials)
                    .description(descWithSpecials)
                    .build();

            when(categoryRepository.findById(20L)).thenReturn(Optional.of(specialCategory));
            when(categoryMapper.mapToDto(specialCategory)).thenReturn(specialCategoryTo);

            // Act
            CategoryTo result = categoryService.getById(20L);

            // Assert
            assertNotNull(result);
            assertEquals(nameWithSpecials, result.getName());
            assertEquals(descWithSpecials, result.getDescription());
        }
    }

    // ========== ТЕСТЫ НАСЛЕДОВАНИЯ ==========

    @Test
    @DisplayName("Должен корректно работать с equals/hashCode BaseTo")
    void shouldWorkCorrectlyWithBaseToEqualsHashCode() {
        // Arrange
        CategoryTo categoryTo1 = CategoryTo.builder().id(1L).name("Test").build();
        CategoryTo categoryTo2 = CategoryTo.builder().id(1L).name("Different").build();

        // Assert - BaseTo.equals сравнивает только по ID
        assertEquals(categoryTo1, categoryTo2);
        assertEquals(categoryTo1.hashCode(), categoryTo2.hashCode());
    }

    // ========== ТЕСТЫ ИММУТАБЕЛЬНОСТИ ==========

    @Test
    @DisplayName("Не должен модифицировать входные параметры")
    void shouldNotModifyInputParameters() {
        // Arrange
        CategoryTo originalTo = CategoryTo.builder()
                .id(1L)
                .name("Original")
                .description("Original Description")
                .build();

        CategoryTo copyTo = CategoryTo.builder()
                .id(1L)
                .name("Original")
                .description("Original Description")
                .build();

        when(categoryMapper.mapToEntity(originalTo)).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);

        // Act
        CategoryTo result = categoryService.save(originalTo);

        // Assert - оригинальный объект не должен измениться
        assertEquals(copyTo, originalTo);
        assertEquals(copyTo.getName(), originalTo.getName());
        assertEquals(copyTo.getDescription(), originalTo.getDescription());
    }

    // ========== ТЕСТЫ ПОРЯДКА ВЫЗОВОВ ==========

    @Test
    @DisplayName("Должен вызывать методы в правильном порядке при сохранении")
    void shouldCallMethodsInCorrectOrderWhenSaving() {
        // Arrange
        when(categoryMapper.mapToEntity(testCategoryTo)).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);

        // Act
        categoryService.save(testCategoryTo);

        // Assert - проверяем порядок вызовов
        var inOrder = inOrder(categoryMapper, categoryRepository);
        inOrder.verify(categoryMapper).mapToEntity(testCategoryTo);
        inOrder.verify(categoryRepository).save(testCategory);
    }

    @Test
    @DisplayName("Должен вызывать методы в правильном порядке при удалении")
    void shouldCallMethodsInCorrectOrderWhenDeleting() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // Act
        categoryService.deleteById(1L);

        // Assert - проверяем порядок вызовов
        var inOrder = inOrder(categoryRepository);
        inOrder.verify(categoryRepository).existsById(1L);
        inOrder.verify(categoryRepository).deleteById(1L);
    }
}