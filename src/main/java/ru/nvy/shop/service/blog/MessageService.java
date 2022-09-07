package ru.nvy.shop.service.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.models.blog.Message;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.repos.blog.MessageRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Message> findAll() {
        return messageRepo.findAll();
    }

    public Iterable<Message> save(String text, String tag, User user, MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);
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
            message.setFilename(resultFilename);
        }
        messageRepo.save(message);
        return messageRepo.findAll();
    }

    public ArrayList<Message> findById(long id) {
        Optional<Message> message = messageRepo.findById(id);
        ArrayList<Message> messages = new ArrayList<>();
        message.ifPresent(messages::add);
        return messages;
    }

    public void updateItem(long id, String text, String tag) {
        Message message = messageRepo.findById(id).orElseThrow();
        if (!StringUtils.isEmpty(tag)) {
            message.setTag(tag);
        }
        message.setText(text);
        messageRepo.save(message);
    }

    public void delete(long id) {
        Message message = messageRepo.findById(id).orElseThrow();
        messageRepo.delete(message);
    }
}