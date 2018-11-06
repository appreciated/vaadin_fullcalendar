package org.vaadin.stefan.fullcalendar;

/**
 * This class can be used to create FullCalendar instances via a fluent builder api. Every method returns
 * an immutable builder instance.
 */
public class FullCalendarBuilder {

    private boolean scheduler;
    private int entryLimit;

    /**
     * Creates a new builder instance with default settings.
     * @return builder instance
     */
    public static FullCalendarBuilder create() {
        return new FullCalendarBuilder(false, -1);
    }

    private FullCalendarBuilder(boolean scheduler, int entryLimit) {
        this.scheduler = scheduler;
        this.entryLimit = entryLimit;
    }

    /**
     * Activates Scheduler support.
     * <p/>
     * <b>Note: </b> You need to add the FullCalender Scheduler extension addon to the class path, otherwise
     * the build will fail.
     * @return new immutable instance with updated settings
     */
    public FullCalendarBuilder withScheduler() {
        return new FullCalendarBuilder(true, entryLimit);
    }

    /**
     * Expects the default limit of entries shown per day. This does not affect basic or
     * list views.
     * <p/>
     * Passing a negative number or 0 disabled the entry limit (same as not using this method at all).
     * @param entryLimit limit
     * @return new immutable instance with updated settings
     */
    public FullCalendarBuilder withEntryLimit(int entryLimit) {
        return new FullCalendarBuilder(scheduler, entryLimit);
    }

    /**
     * Builds the FullCalendar with the settings of this instance. Depending on some settings the returned
     * instance might be a subclass of {@link FullCalendar}.
     * @return FullCalendar instance
     */
    public FullCalendar build() {
        if (scheduler) {
            try {
                Class<?> loadClass = getClass().getClassLoader().loadClass("org.vaadin.stefan.fullcalendar.FullCalendarScheduler");
                return (FullCalendar) loadClass.getDeclaredConstructor(int.class).newInstance(entryLimit);
            } catch (ClassNotFoundException ce) {
                throw new RuntimeException("Could not find scheduler extension for FullCalendar on class path. Please check you libraries / dependencies.", ce);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new FullCalendar(entryLimit);
        }
    }

}
