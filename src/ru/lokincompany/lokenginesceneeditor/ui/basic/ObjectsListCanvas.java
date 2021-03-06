package ru.lokincompany.lokenginesceneeditor.ui.basic;

import ru.lokincompany.lokengine.gui.additionalobjects.GUIObjectProperties;
import ru.lokincompany.lokengine.gui.additionalobjects.guipositions.GUIPosition;
import ru.lokincompany.lokengine.gui.canvases.GUICanvas;
import ru.lokincompany.lokengine.gui.canvases.GUIScrollCanvas;
import ru.lokincompany.lokengine.gui.guiobjects.GUIButton;
import ru.lokincompany.lokengine.gui.guiobjects.GUIFreeTextDrawer;
import ru.lokincompany.lokengine.gui.guiobjects.GUIPanel;
import ru.lokincompany.lokengine.gui.guiobjects.GUIText;
import ru.lokincompany.lokengine.render.frame.PartsBuilder;
import ru.lokincompany.lokengine.sceneenvironment.defaultenvironment.Scene;
import ru.lokincompany.lokengine.sceneenvironment.defaultenvironment.SceneObject;
import ru.lokincompany.lokengine.tools.FontPrefs;
import ru.lokincompany.lokengine.tools.Logger;
import ru.lokincompany.lokengine.tools.color.Color;
import ru.lokincompany.lokengine.tools.vectori.Vector2i;
import ru.lokincompany.lokenginesceneeditor.LESEApplication;
import ru.lokincompany.lokenginesceneeditor.sceneintegration.HighlightedObject;
import ru.lokincompany.lokenginesceneeditor.ui.Colors;

public class ObjectsListCanvas extends GUICanvas {

    GUIScrollCanvas scrollCanvas;
    GUIFreeTextDrawer textDrawer;
    GUIText textObjectsCount;
    GUIButton buttonAddObject;
    GUIPanel panel;
    Scene scene;
    Vector2i textGap = new Vector2i(0, 15);

    public ObjectsListCanvas(Vector2i position, Vector2i size, Scene scene) {
        super(position, size);
        this.scene = scene;

        panel = new GUIPanel().setSize(guiObject -> this.getSize());

        scrollCanvas = new GUIScrollCanvas(new Vector2i(0, 20), new Vector2i(), new Vector2i(), null);
        scrollCanvas.setSize(guiObject -> new Vector2i(this.getSize().x, this.getSize().y - guiObject.getPosition().y));

        textDrawer = new GUIFreeTextDrawer(new FontPrefs().setSize(12));
        textObjectsCount = new GUIText(new FontPrefs()).setText("0 объектов");

        scrollCanvas.addObject(textDrawer);

        buttonAddObject = new GUIButton()
                .setText(new GUIText().setText("+"))
                .setUnpressScript(guiButton -> {
                        SceneObject object = new SceneObject();
                        object.position.x = LESEApplication.getInstance().window.getCamera().position.x;
                        object.position.y = LESEApplication.getInstance().window.getCamera().position.y;
                        scene.addObject(object);
                })
                .setSize(new Vector2i(20, 20));

        this.addObject(panel);
        this.addObject(buttonAddObject);
        this.addObject(textObjectsCount, GUIPosition.TopRight);
        this.addObject(scrollCanvas);
    }

    @Override
    public void update(PartsBuilder partsBuilder, GUIObjectProperties parentProperties) {
        super.update(partsBuilder, parentProperties);

        int objectsCount = scene.getCountObjects();

        textObjectsCount.setText(objectsCount + " объектов");
        int windowY = parentProperties.window.getResolution().y;
        for (int i = 0; i < objectsCount; i++) {
            SceneObject sceneObject = scene.getObjectByID(i);
            Vector2i textPos = new Vector2i(2, textGap.y * i);
            Vector2i globalPos = new Vector2i(scrollCanvas.properties.globalPosition.x + textPos.x, scrollCanvas.properties.globalPosition.y + textPos.y);
            if (globalPos.y > 0 && globalPos.y < windowY) {
                boolean selected = parentProperties.window.getMouse().inField(globalPos, new Vector2i(getSize().x, textGap.y));

                if (selected && parentProperties.window.getMouse().getPressedStatus()) {
                    HighlightedObject.highlight(sceneObject, i);
                }

                textDrawer.draw(sceneObject.name + " [" + i + "]", textPos, selected || HighlightedObject.getHighlightedObjectID() == i ? Colors.engineBrightMainColor() : Colors.engineMainColor());
            }
        }
    }
}
