package com.dotcms.datagen;

import com.dotcms.repackage.com.google.common.base.Strings;
import com.dotcms.repackage.com.google.common.collect.ImmutableMap;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.templates.business.TemplateAPI;
import com.dotmarketing.portlets.templates.model.Template;
import com.liferay.util.StringPool;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class used to create {@link Template} objects for test purposes
 *
 * @author Nollymar Longa
 */
public class TemplateDataGen extends AbstractDataGen<Template> {

    private long currentTime = System.currentTimeMillis();
    private String body;
    private String footer;
    private String header;
    private String friendlyName = "testFriendlyName" + currentTime;
    private String image;
    private String title = "testTitle" + currentTime;
    private List<Map<String, String>> containers = new ArrayList<>();

    private static final TemplateAPI templateAPI = APILocator.getTemplateAPI();
    private static final String type = "template";

    /**
     * Sets body property to the TemplateDataGen instance. This will be used when a new {@link
     * Template} instance is created.
     *
     * <p><b>Important</b>: this will override the auto-generated body with the added containers.
     * See {@link #withContainer(String, String)}
     *
     * @param body the body of the template
     * @return TemplateDataGen with body property set
     */
    public TemplateDataGen body(String body) {
        this.body = body;
        return this;
    }

    /**
     * Sets footer property to the TemplateDataGen instance. This will be used when a new {@link
     * Template} instance is created
     *
     * @param footer the footer of the template
     * @return TemplateDataGen with footer property set
     */
    public TemplateDataGen footer(String footer) {
        this.footer = footer;
        return this;
    }

    /**
     * Sets friendlyName property to the TemplateDataGen instance. This will be used when a new
     * {@link Template} instance is created
     *
     * @param friendlyName the friendly name for the template
     * @return TemplateDataGen with friendlyName property set
     */
    public TemplateDataGen friendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
        return this;
    }

    /**
     * Sets header property to the TemplateDataGen instance. This will be used when a new {@link
     * Template} instance is created
     *
     * @param header the header of the template
     * @return TemplateDataGen with header property set
     */
    public TemplateDataGen header(String header) {
        this.header = header;
        return this;
    }

    /**
     * Sets image property to the TemplateDataGen instance. This will be used when a new {@link
     * Template} instance is created
     *
     * @param image the image associated with this template
     * @return TemplateDataGen with image property set
     */
    public TemplateDataGen image(String image) {
        this.image = image;
        return this;
    }

    /**
     * Sets title property to the TemplateDataGen instance. This will be used when a new {@link
     * Template} instance is created
     *
     * @param title the title of this template
     * @return TemplateDataGen with title property set
     */
    public TemplateDataGen title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Adds a container to the list of containers to be included in the {@link Template} this
     * data-gen will create.
     *
     * <p>The {@link #next()} method will include a '#parseContainer('containerId')' string to the
     * body of the template for each of the containers added to this {@link TemplateDataGen}.
     *
     * @param containerId the container identifier to include
     * @return the data-gen with the added container
     */
    public TemplateDataGen withContainer(final String containerId, final String UUID) {
        Map<String, String> containerMap = new ImmutableMap.Builder<String, String>()
                .put("containerId", containerId).put("uuid", UUID).build();
        containers.add(containerMap);
        return this;
    }

    public TemplateDataGen withContainer(final String containerId) {
        Map<String, String> containerMap = new ImmutableMap.Builder<String, String>()
                .put("containerId", containerId).put("uuid", UUID.randomUUID().toString()).build();
        containers.add(containerMap);
        return this;
    }

    /**
     * Removes a container from the list of containers be included to the {@link Template} this
     * data-gen will create
     *
     * @param container the container to remove
     * @return the data-gen with the removed container
     */
    public TemplateDataGen withoutContainer(Container container) {
        containers.remove(container);
        return this;
    }

    /**
     * Clears the list of containers be included to the {@link Template} this data-gen will create
     *
     * @return the data-gen with the cleared list of containers
     */
    public TemplateDataGen clearContainers() {
        containers.clear();
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>It will also include a '#parseContainer('containerId')' string to the body of the
     * template for each of the containers added to this {@link TemplateDataGen} by calling {@link
     * #withContainer(String, String)}
     */
    @Override
    public Template next() {
        // Create the new template
        Template template = new Template();
        template.setFooter(this.footer);
        template.setFriendlyName(this.friendlyName);
        template.setHeader(this.header);
        template.setIDate(new Date());
        template.setImage(this.image);
        template.setModDate(new Date());
        template.setModUser(user.getUserId());
        template.setOwner(user.getUserId());
        template.setShowOnMenu(true);
        template.setSortOrder(2);
        template.setTitle(this.title);
        template.setType(type);
        template.setBody(body);
        return template;
    }

    @Override
    public Template persist(Template template) {

        if (Strings.isNullOrEmpty(body)) {

            if (containers.isEmpty()) {
                withContainer(new ContainerDataGen().nextPersisted().getIdentifier());
            }

            // include the parseContainer for every container added
            StringBuilder sb = new StringBuilder();
            for (Map<String, String> containerMap : containers) {
                sb.append("#parseContainer(")
                        .append(StringPool.APOSTROPHE)
                        .append(containerMap.get("containerId"))
                        .append(StringPool.APOSTROPHE)
                        .append(StringPool.COMMA)
                        .append(StringPool.APOSTROPHE)
                        .append(containerMap.get("uuid"))
                        .append(StringPool.APOSTROPHE)
                        .append(")")
                        .append(System.getProperty("line.separator"));
            }

            template.setBody(sb.toString());
        } else {
            template.setBody(body);
        }

        try {
            return templateAPI.saveTemplate(template, host, user, false);
        } catch (DotDataException | DotSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void remove(Template template) {
        try {
            templateAPI.delete(template, user, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}