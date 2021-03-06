package ru.lokincompany.lokenginesceneeditor;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Checks;
import ru.lokincompany.lokengine.applications.ApplicationDefault;
import ru.lokincompany.lokengine.tools.color.Color;
import ru.lokincompany.lokengine.tools.saveworker.FileWorker;
import ru.lokincompany.lokengine.tools.vectori.Vector2i;
import ru.lokincompany.lokenginesceneeditor.sceneintegration.HighlightedObject;
import ru.lokincompany.lokenginesceneeditor.sceneintegration.KeyboardBinds;
import ru.lokincompany.lokenginesceneeditor.scenetester.SceneTestApplication;
import ru.lokincompany.lokenginesceneeditor.ui.basic.notification.notificationtypes.NotificationError;
import ru.lokincompany.lokenginesceneeditor.ui.basic.notification.notificationtypes.NotificationSuccess;
import ru.lokincompany.lokenginesceneeditor.ui.basic.notification.notificationtypes.NotificationWarning;
import ru.lokincompany.lokenginesceneeditor.ui.composite.SceneEditor;

public class LESEApplication extends ApplicationDefault {

    private static LESEApplication instance;
    public SceneEditor sceneEditor;

    LESEApplication() {
        start(false, true, true, 32, new Vector2i(1280, 720), "LokEngine Scene Editor");
        instance = this;
    }


    public static LESEApplication getInstance() {
        return instance;
    }

    public void runTest(){
        new SceneTestApplication(scene.save());
    }

    public void loadScene(String path) {
        scene.removeAll();
        HighlightedObject.highlight(null, -1);
        try {
            if (FileWorker.fileExists(path)) {
                FileWorker fileWorker = new FileWorker(path);
                fileWorker.openRead();
                scene.load(fileWorker.read());
                fileWorker.close();

                sceneEditor.notificationListCanvas.addNotification(new NotificationSuccess("Загрузка успешна!\nОбъектов загружено: " + scene.getCountObjects()));
            } else {
                sceneEditor.notificationListCanvas.addNotification(new NotificationWarning("Файла сцены не существует по заданному пути!"));
            }
        } catch (Exception e) {
            sceneEditor.notificationListCanvas.addNotification(new NotificationError("Загрузить сцену не удалось!"));
            e.printStackTrace();
        }
    }

    public void saveScene(String path) {
        try {
            FileWorker fileWorker = new FileWorker(path);
            fileWorker.openWrite();
            fileWorker.write(scene.save());
            fileWorker.close();

            sceneEditor.notificationListCanvas.addNotification(new NotificationSuccess("Сохранение успешно!"));
        } catch (Exception e) {
            sceneEditor.notificationListCanvas.addNotification(new NotificationError("Сохранить сцену не удалось!"));
            e.printStackTrace();
        }
    }

    @Override
    protected void updateEvent() {
        HighlightedObject.update();
        KeyboardBinds.update();
    }

    @Override
    protected void initEvent() {
        applicationRuntime.setSpeedEngine(0);
        window.getFrameBuilder().backgroundColor = new Color(0.15F, 0.15F, 0.15F, 1.0F);
        window.setCloseEvent((window1, objects) -> close());

        sceneEditor = new SceneEditor();
        canvas.addObject(sceneEditor);
    }
}
