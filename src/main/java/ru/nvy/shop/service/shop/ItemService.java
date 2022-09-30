package ru.nvy.shop.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.repos.shop.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void save(Item item, MultipartFile file) throws IOException {
        if (file != null) {
            File uploadDir = new File(uploadPath);
            // если не существует директории
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // создадим ее
            }
            // создаем уникальное имя файла, чтобы избавиться от коллизии
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));
            item.setFilename(resultFilename);
        }
        itemRepository.save(item);
    }

    public void delete(long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        itemRepository.delete(item);
    }

    public ArrayList<Item> findById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        return items;
    }

    public void updateItem(long id, String name, int cost, String description) {
        Item item = itemRepository.findById(id).orElseThrow();
        if (!StringUtils.isEmpty(name)) {
            item.setName(name);
        }
        item.setCost(cost);
        item.setDescription(description);
        itemRepository.save(item);
    }
}