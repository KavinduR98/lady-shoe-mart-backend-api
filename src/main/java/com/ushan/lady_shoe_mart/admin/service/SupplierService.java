package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Supplier;
import com.ushan.lady_shoe_mart.admin.repository.SupplierRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SupplierService implements ISupplierService{

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ApiResponse<Supplier> save(Supplier supplier) {
        ApiResponse<Supplier> response = new ApiResponse<>();
        if (supplier.getSupplierId() == null) throw new LsmException("Supplier code can't be empty!");
        if (supplier.getEmail() == null || !supplier.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$"))
            throw new LsmException("Email not validate");
        if (supplier.getMobile() == null || !supplier.getMobile().matches("^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$"))
            throw new LsmException("Mobile not validate");
        if (supplier.getActive() == null) throw new LsmException("Active can't be empty");
        com.ushan.lady_shoe_mart.admin.entity.Supplier supplierFindBySupplierId = supplierRepository.findBySupplierIdIgnoreCase(supplier.getSupplierId());
        if (supplierFindBySupplierId != null)
            throw new LsmException("Supplier code exist, change the supplier code and retry!");
        com.ushan.lady_shoe_mart.admin.entity.Supplier supplierDao = modelMapper.map(supplier, com.ushan.lady_shoe_mart.admin.entity.Supplier.class);
        supplierDao.setSupplierId(supplierDao.getSupplierId().toUpperCase());
        supplierDao.setDateCreated(new Date());
        supplierDao.setDateUpdated(new Date());
        supplierDao.setIsActive(true);
        supplierRepository.save(supplierDao);
        response.setMessage("Supplier saved successfully");
        response.setObject(modelMapper.map(supplierDao, Supplier.class));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Supplier> findAllSupplier() {
        List<com.ushan.lady_shoe_mart.admin.entity.Supplier> supplierList = supplierRepository.findAllByIsActiveIsTrue();
        return supplierList.stream().map(supplier -> modelMapper.map(supplier, Supplier.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Supplier> getSupplierById(Long id) {
        ApiResponse<Supplier> response = new ApiResponse<>();
        com.ushan.lady_shoe_mart.admin.entity.Supplier supplierDao = supplierRepository.findByIdAndIsActiveTrue(id);
        response.setStatus(HttpStatus.OK.value());
        response.setObject(modelMapper.map(supplierDao, Supplier.class));
        return response;
    }

    @Transactional
    @Override
    public  ApiResponse<Boolean> active(Long id, Boolean active) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (active == null) throw new LsmException("Active status can't be empty");
        com.ushan.lady_shoe_mart.admin.entity.Supplier supplierDao = findSupplierById(id);
        if (supplierDao.getActive() == active) throw new LsmException("Supplier active status already updated");
        supplierDao.setActive(active);
        supplierDao.setDateUpdated(new Date());
        supplierRepository.save(supplierDao);
        log.info("Supplier active status updated successful : " + supplierDao.getId());
        response.setMessage("Supplier active status updated successful");
        response.setObject(Boolean.TRUE);
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    private com.ushan.lady_shoe_mart.admin.entity.Supplier findSupplierById(Long id) {
        if (id == null || id == 0) throw new LsmException("Id can't be empty");
        Optional<com.ushan.lady_shoe_mart.admin.entity.Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isEmpty()) throw new LsmException("Supplier not found");
        return optionalSupplier.get();
    }
}
