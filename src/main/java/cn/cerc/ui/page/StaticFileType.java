package cn.cerc.ui.page;

public enum StaticFileType {
    icon(StaticFileGroup.低频更新), productImage(StaticFileGroup.低频更新), otherImage(StaticFileGroup.低频更新),
    cssFile(StaticFileGroup.中频更新), jsFile(StaticFileGroup.中频更新), imageFile(StaticFileGroup.中频更新),
    summerImage(StaticFileGroup.高频更新), menuImage(StaticFileGroup.中频更新);

    private StaticFileGroup group;

    public StaticFileGroup getGroup() {
        return group;
    }

    StaticFileType(StaticFileGroup group) {
        this.group = group;
    }

}
