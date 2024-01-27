package com.dobiasdev.spa.converters;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@Setter
public class NavigationFields {
    private String name;
    private List<NavigationFields> children = new ArrayList<>();

    public NavigationFields() {
    }

    public NavigationFields(String name) {
        this.name = name;
    }

    public void navigate(String field, Consumer<NavigationFields> navigateAction)
    {
        children.stream()
                .filter(n -> Objects.equals(n.getName(), field))
                .findFirst()
                .ifPresent(navigateAction);
    }

    public static NavigationFields create(String settings)
    {
        if(Strings.isNullOrEmpty(settings))
            return none();

        return create(settings.split(","));
    }

    public static NavigationFields create(String[] paths)
    {
        if(paths == null)
            return none();

        var root = new NavigationFields();

        for (String path : paths) {
            var fields = path.split("\\.");

            createNode(root, fields);
        }

        return root;
    }

    public static NavigationFields none()
    {
        return new NavigationFields();
    }

    private static void createNode(NavigationFields node, String[] fields)
    {
        if(fields.length == 0)
            return;

        var child = node.getChildren().stream().filter(n -> Objects.equals(n.getName(), fields[0])).findFirst().orElse(null);
        if(child == null)
        {
            child = new NavigationFields(fields[0]);
            node.getChildren().add(child);
        }

        if(fields.length > 1)
            createNode(child, Arrays.copyOfRange(fields, 1, fields.length));
    }
}
