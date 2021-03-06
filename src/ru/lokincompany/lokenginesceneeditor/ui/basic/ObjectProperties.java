package ru.lokincompany.lokenginesceneeditor.ui.basic;

import ru.lokincompany.lokengine.gui.additionalobjects.GUILocationAlgorithm;
import ru.lokincompany.lokengine.gui.additionalobjects.GUIObjectProperties;
import ru.lokincompany.lokengine.gui.additionalobjects.guipositions.GUIPosition;
import ru.lokincompany.lokengine.gui.canvases.GUICanvas;
import ru.lokincompany.lokengine.gui.canvases.GUIListCanvas;
import ru.lokincompany.lokengine.gui.guiobjects.*;
import ru.lokincompany.lokengine.render.Sprite;
import ru.lokincompany.lokengine.render.frame.PartsBuilder;
import ru.lokincompany.lokengine.sceneenvironment.defaultenvironment.SceneObject;
import ru.lokincompany.lokengine.sceneenvironment.defaultenvironment.components.*;
import ru.lokincompany.lokengine.sceneenvironment.defaultenvironment.components.componentstools.ShapeCreator;
import ru.lokincompany.lokengine.tools.FontPrefs;
import ru.lokincompany.lokengine.tools.color.Color;
import ru.lokincompany.lokengine.tools.vectori.Vector2i;
import ru.lokincompany.lokenginesceneeditor.LESEApplication;
import ru.lokincompany.lokenginesceneeditor.sceneintegration.HighlightedObject;
import ru.lokincompany.lokenginesceneeditor.ui.Colors;
import ru.lokincompany.lokenginesceneeditor.ui.basic.objectcomponents.ObjectComponents;

public class ObjectProperties extends GUICanvas {
    GUIPanel panel;
    GUIButton addButton;

    GUIListCanvas textFields;
    GUIListCanvas texts;
    ObjectComponents componentsList;

    GUITextField nameField;
    SceneObject sceneObject;

    public ObjectProperties(Vector2i position, Vector2i size) {
        super(position, size);

        panel = new GUIPanel();
        panel.setSize(guiObject -> this.getSize());

        nameField = new GUITextField().setCentralizeText(true).setSize(new Vector2i(size.x, 20));

        texts = new GUIListCanvas(new Vector2i(3, 30), new Vector2i(75, 100), new Vector2i(75, 14));
        textFields = new GUIListCanvas(new Vector2i(21, 30), new Vector2i(this.getSize().x - 18, 100), new Vector2i(this.getSize().x - 18, 14));

        addButton = new GUIButton()
                .setText(new GUIText(new FontPrefs().setSize(10)).setText("Добавить компонент"))
                .setUnpressScript(guiButton -> LESEApplication.getInstance().sceneEditor.selectComponentWindow.sendRequest(
                        componentName -> {
                            switch (componentName) {
                                case "Sprite component":
                                    sceneObject.components.add(new SpriteComponent(""));
                                    break;
                                case "Animation component":
                                    sceneObject.components.add(new AnimationComponent());
                                    break;
                                case "Rigidbody component":
                                    sceneObject.components.add(new RigidbodyComponent(ShapeCreator.CreateCircleShape(100)));
                                    break;
                                case "Sound component":
                                    try {
                                        sceneObject.components.add(new SoundComponent());
                                    } catch (Exception e) {
                                    }
                                    break;
                                case "Particle System component":
                                    sceneObject.components.add(new ParticleSystemComponent(new Sprite("")));
                                    break;
                            }
                        })
                )
                .setPosition(new Vector2i(0, texts.getPosition().y + texts.getSize().y))
                .setSize(guiObject -> new Vector2i(this.getSize().x, 14));

        componentsList = new ObjectComponents(new Vector2i(0, addButton.getPosition().y + addButton.getSize().y), new Vector2i());
        componentsList.setSize(guiObject -> new Vector2i(this.getSize().x, this.getSize().y - componentsList.getPosition().y));

        GUILocationAlgorithm fieldSize = guiObject -> new Vector2i(textFields.getSize().x, 14);
        Color fieldBackground = new Color(0, 0, 0, 0);

        GUITextField XField = new GUITextField();
        XField.setSize(fieldSize);
        XField.setBackgroundColor(fieldBackground);

        GUITextField YField = new GUITextField();
        YField.setSize(fieldSize);
        YField.setBackgroundColor(fieldBackground);

        GUITextField RField = new GUITextField();
        RField.setSize(fieldSize);
        RField.setBackgroundColor(fieldBackground);

        GUITextField RPField = new GUITextField();
        RPField.setSize(fieldSize);
        RPField.setBackgroundColor(fieldBackground);

        XField.setStatusChangedScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    sceneObject.position.x = Float.parseFloat(guiTextField.getText());
                } catch (Exception e) {
                }
        });

        XField.setInactiveScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    guiTextField.setText(String.valueOf(sceneObject.position.x));
                } catch (Exception e) {
                }
        });

        YField.setStatusChangedScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    sceneObject.position.y = Float.parseFloat(guiTextField.getText());
                } catch (Exception e) {
                }
        });

        YField.setInactiveScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    guiTextField.setText(String.valueOf(sceneObject.position.y));
                } catch (Exception e) {
                }
        });

        RField.setStatusChangedScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    sceneObject.rollRotation = Float.parseFloat(guiTextField.getText());
                } catch (Exception e) {
                }
        });

        RField.setInactiveScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    guiTextField.setText(String.valueOf(sceneObject.rollRotation));
                } catch (Exception e) {
                }
        });

        RPField.setStatusChangedScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    sceneObject.renderPriority = Float.parseFloat(guiTextField.getText());
                } catch (Exception e) {
                }
        });

        RPField.setInactiveScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    guiTextField.setText(String.valueOf(sceneObject.renderPriority));
                } catch (Exception e) {
                }
        });

        nameField.setStatusChangedScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    sceneObject.name = guiTextField.getText();
                } catch (Exception e) {
                }
        });

        nameField.setInactiveScript(guiTextField -> {
            if (sceneObject != null)
                try {
                    guiTextField.setText(sceneObject.name);
                } catch (Exception e) {
                }
        });

        textFields.addObject(XField);
        textFields.addObject(YField);
        textFields.addObject(new GUISpace(new Vector2i(), new Vector2i(0, 4)));
        textFields.addObject(RField);
        textFields.addObject(new GUISpace(new Vector2i(), new Vector2i(0, 4)));
        textFields.addObject(RPField);

        texts.addObject(new GUIText().setText("X:"));
        texts.addObject(new GUIText().setText("Y:"));
        texts.addObject(new GUISpace(new Vector2i(), new Vector2i(0, 4)));
        texts.addObject(new GUIText().setText("R:"));
        texts.addObject(new GUISpace(new Vector2i(), new Vector2i(0, 4)));
        texts.addObject(new GUIText(new FontPrefs().setSize(10)).setText("RP:"));

        this.addObject(panel);
        this.addObject(nameField, GUIPosition.TopCenter);
        this.addObject(texts);
        this.addObject(textFields);
        this.addObject(addButton);
        this.addObject(componentsList);
    }

    @Override
    public void update(PartsBuilder partsBuilder, GUIObjectProperties parentProperties) {
        super.update(partsBuilder, parentProperties);
        sceneObject = HighlightedObject.getHighlightedObject();
    }

}
