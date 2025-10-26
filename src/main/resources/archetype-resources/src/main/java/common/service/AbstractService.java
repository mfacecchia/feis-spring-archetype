package ${package}.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import ${package}.common.data.dto.BasePageDto;
import ${package}.common.data.entity.BaseAuditingEntity;
import ${package}.common.exception.ResourceNotFoundException;
import ${package}.common.exception.ValidationException;
import ${package}.common.exception.model.Error;
import ${package}.common.exception.model.InternalErrorCode;
import ${package}.common.exception.model.ValidationError;
import ${package}.common.mapper.BaseMapper;
import ${package}.common.repository.BaseRepository;
import ${package}.common.specification.CommonSpecificationBuilder;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;


public abstract class AbstractService<ENTITY extends BaseAuditingEntity, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE> {
    @Autowired
    protected Validator validator;

    protected static Logger logger;

    protected BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> mapper;
    protected BaseRepository<ENTITY, PK_TYPE> repository;
    protected String resourceName = "Resource";

    protected abstract void validateCreateDto(CREATE_DTO createDto);

    protected abstract void validateUpdateDto(UPDATE_DTO updateDto, ENTITY existing);

    protected abstract void validateDelete(PK_TYPE id);

    protected abstract PK_TYPE getResourceId(ENTITY entity);

    public GET_DTO get(PK_TYPE id) {
        ENTITY entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        logger.info("GetById ::: {} found with id {}", resourceName, id);

        return convertToDto(entity);
    }

    public PAGEABLE_DTO getAll(CommonSpecificationBuilder<ENTITY> specificationBuilder, Pageable pageable, boolean showTotalCount) {
        Specification<ENTITY> specification = specificationBuilder.build();

        if (pageable != null) {
            Page<GET_DTO> entityPage = repository.findAll(specification, pageable).map(mapper::mapToDto);

            PAGEABLE_DTO pageableDto = convertToPageDto(entityPage);

            if (showTotalCount) {
                long totalCount = entityPage.getTotalElements();
                pageableDto.setTotalCount(totalCount);
            }

            logger.info("GetAll ::: Found {} total results. Displaying first {} items", entityPage.getTotalElements(), entityPage.getNumberOfElements());

            return pageableDto;
        }

        List<GET_DTO> itemsList = repository.findAll(specification).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        Page<GET_DTO> itemsPage;
        if (itemsList.isEmpty()) {
            itemsPage = Page.empty();
        } else {
            pageable = PageRequest.of(0, itemsList.size());
            itemsPage = new PageImpl<>(itemsList, pageable, itemsList.size());
        }

        logger.info("GetAll ::: Found {} total results.", itemsList.size());

        return convertToPageDto(itemsPage);
    }

    @Transactional
    public GET_DTO create(CREATE_DTO createDto) {
        doValidate(createDto);
        validateCreateDto(createDto);
        ENTITY entity = convertToEntity(createDto);
        doCreate(entity);
        ENTITY saved = save(entity);

        logger.info("Create ::: Created new {} with id", resourceName, getResourceId(saved));

        return convertToDto(saved);
    }

    @Transactional
    public GET_DTO update(PK_TYPE id, UPDATE_DTO updateDto) {
        ENTITY existing = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));

        doValidate(updateDto);
        validateUpdateDto(updateDto, existing);

        convertUpdateDtoToEntity(updateDto, existing);
        doUpdate(existing, updateDto);
        ENTITY saved = save(existing);

        logger.info("Update ::: Updated {} with id ", resourceName, id);

        return convertToDto(saved);
    }

    @Transactional
    public void delete(PK_TYPE id) {
        validateDelete(id);
        // Soft delete implemented by default. If you want to hard delete, override this
        // method in the service class
        softDelete(id);
    }

    protected void softDelete(PK_TYPE id) {
        Optional<ENTITY> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return;
        }

        ENTITY toDelete = existing.get();
        doDelete(toDelete);

        toDelete.setDeleted(true);
        save(toDelete);

        logger.info("Delete ::: Execute soft delete on {} with id {}", resourceName, id);
    }

    protected void hardDelete(PK_TYPE id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }

        logger.info("Delete ::: Execute hard delete on {} with id {}", resourceName, id);
    }

    protected ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    protected void doValidate(Object dto) {
        Set<ConstraintViolation<Object>> validationErrors = validator.validate(dto);
        List<Error> appValidationerrors = new ArrayList<>();

        for (ConstraintViolation<?> validationError : validationErrors) {
            String fieldName = validationError.getPropertyPath().iterator().next().getName();
            appValidationerrors.add(
                    new ValidationError(fieldName, InternalErrorCode.PARAMETER_INVALID, validationError.getMessage()));
        }
        if (appValidationerrors.size() >= 1) {
            throw new ValidationException(appValidationerrors);
        }
    }

    protected void doCreate(ENTITY toCreate) {
    }

    protected void doUpdate(ENTITY toUpdate, UPDATE_DTO updateDto) {
    }

    protected void doDelete(ENTITY entity) {
    }

    public GET_DTO convertToDto(ENTITY entity) {
        return mapper.mapToDto(entity);
    }

    public ENTITY convertDtoToEntity(GET_DTO getDto) {
        return mapper.mapToEntity(getDto);
    }

    public PAGEABLE_DTO convertToPageDto(Page<GET_DTO> itemPage) {
        return mapper.mapPageToPageableDto(itemPage);
    }

    public ENTITY convertToEntity(CREATE_DTO createDto) {
        return mapper.mapCreateDtoToEntity(createDto);
    }


    public ENTITY convertUpdateDtoToEntity(UPDATE_DTO updateDto, ENTITY toUpdate) {
        mapper.mapUpdateDtoToEntity(updateDto, toUpdate);
        return toUpdate;
    }

    public UPDATE_DTO convertToUpdateDto(ENTITY entity) {
        return mapper.mapToUpdateDto(entity);
    }
}
