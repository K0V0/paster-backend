package com.kovospace.paster.item.services;

import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemServiceUtils {

    @Value("${item.free-user-items-limit}")
    private static int FREE_USER_ITEMS_LIMIT = 20;

    @Value("${item.file-chunk-size}")
    private static long DEFAULT_CHUNK_SIZE = 524288;

    private static final Pattern fileExtensionPattern = Pattern.compile(".+\\.([a-zA-Z0-9]+)");

    public static String getFileExtension(String originalFileName) {
        Matcher matcher = fileExtensionPattern.matcher(originalFileName);
        if (!matcher.matches()) return null;
        return matcher.group(1);
    }

    public static Item prepareNewItem(
            long userId,
            String platform,
            String deviceName,
            UserRepository userRepository
    )
            throws UserNotFoundException
    {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = new Item();
        item.setUser(user);
        item.setPlatform(PlatformEnum.getByName(platform));
        item.setDeviceName(deviceName);
        return item;
    }


    public static void cleanOldItems(
            User user,
            ItemRepository itemRepository)
    {
        List<Item> items = itemRepository.findAllByUserOrderByCreatedAtDesc(user);
        if (items.size() > FREE_USER_ITEMS_LIMIT) {
            itemRepository.deleteAll(items.subList(20, items.size()));
        }
    }


    public static File prepareNewFile(
            String originalFileName,
            String mimeType,
            Long chunksCount,
            Long chunkSize
    ) {
        File file = new File();
        /** compulsory */
        file.setOriginalFileName(originalFileName);
        file.setMimeType(mimeType);
        /** optional */
        file.setExtension(getFileExtension(originalFileName));
        file.setChunksCount(chunksCount == null ? 1 : chunksCount);
        file.setChunkSize(chunkSize == null ? DEFAULT_CHUNK_SIZE : chunkSize);
        return file;
    }
}
