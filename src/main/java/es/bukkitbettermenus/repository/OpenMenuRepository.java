package es.bukkitbettermenus.repository;

import es.bukkitbettermenus.Menu;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OpenMenuRepository {
    private final Map<Class<? extends Menu>, List<Menu>> menusByType;
    private final Map<String, Menu> menusByPlayerName;

    public OpenMenuRepository() {
        this.menusByPlayerName = new ConcurrentHashMap<>();
        this.menusByType = new ConcurrentHashMap<>();
    }

    public void save(String jugador, Menu menu){
        this.menusByType.putIfAbsent(menu.getClass(), new LinkedList<>());
        this.menusByType.get(menu.getClass()).add(menu);
        this.menusByPlayerName.put(jugador, menu);
    }

    public Optional<Menu> findByPlayerName(String playerName){
        return Optional.ofNullable(this.menusByPlayerName.get(playerName));
    }

    public List<Menu> findByMenuType(Class<? extends Menu> menuType){
        List<Menu> menus = this.menusByType.get(menuType);

        return menus == null ? Collections.EMPTY_LIST : menus;
    }

    public void deleteByPlayerName(String playerName, Class<? extends Menu> menuType){
        Menu menuRemoved = this.menusByPlayerName.remove(playerName);
        this.menusByType.get(menuType).removeIf(menu -> menu.equals(menuRemoved));
    }
}
