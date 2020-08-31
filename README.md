## Two plugin for jedit

### Plugin MyDuck
The least possible to be a plugin - simpler than quicknotepad


### Plugin TxtConv
markdown plugin, using [txtmark](https://github.com/rjeschke/txtmark) and [jsoup](https://jsoup.org/) for various purposes. Characteristics for plugin TxtConv

- does only makes html from markdown despite its name.
- has javadoc in source.
- converts a .md file to a .html file in same directory using conversion from markdown to html.
- deal with a _basedir_ in which internal links are managed 
- can convert current buffer to a new Untitled buffer
- has a spinner in status bar to show progress
- convenient 'shift to browser' functionality made as an option to minimize jedit on exit of markdown conversion. This option must not be selected when multible markdown conversions will occur, such as one or more generates of some .bsh files not being uptodate 
- uses some _dirty tricks_ to coop with markdown inside html block elements, somthing than txtmark in itself would not convert.
- has a _rendering mechanism_ for including css and js and offers a choice of letting the converted markdown have one of following of relations to the body element:
	- a child of
	- a child of some element in an unlimmited complex child of
	

late reveiw: a bit complicated



~~~c
struct {
    void (*example)(void);
    string name;
    } examples[] = { 
         {example_OLED,"OLED"}
        ,{example_stick,"stick"}
        ,{example_BinaryRows,"binaryRows"}
        ,{example_clock,"clock"} // 17:31
        ,{example_secClock,"sec-clock"} // 17:31:00
        ,{example_assert,"assertion"}
        ,{example_flag,"flag"}
        ,{example_peek,"peek"}
        };

void showExample(string name) {
    for (int8_t i = 0; i < sizeof(examples)/sizeof(examples[0]); i++)
        if (name == examples[i].name) 
            (examples[i].example)();
        
}
~~~

~~~c++
OLED::OLED() : 
     i2c(I2C_SDA0,I2C_SCL0)
    ,charX(0)
    ,charY(0)
    ,displayWidth(128)
    ,displayHeight(64 / 8)
    ,screenSize(0) 
    ,pendingNewline(false)
    {}

void OLED::command(uint8_t cmd) {
    char buf[2];
    buf[0] = '\0';
    buf[1] = cmd;
    i2c.write(chipAdress,buf,2);
}
~~~

~~~java
private void unzip(String zipFile, String destDirectory) {
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
        destDir.mkdir();
        try (ZipInputStream zis = new ZipInputStream(this.getClass().getResourceAsStream(zipFile))) {
            ZipEntry entry;
            while (null != (entry = zis.getNextEntry())) {
                extractFile(zis, destDirectory+"/"+entry.getName());
                zis.closeEntry();
            }
        } catch(IOException e) { e.printStackTrace();}
    }
}
~~~

~~~php
function listOverPic($secTmpl) {
    global $page;
    extract($secTmpl);
    foreach ($page->recordsOfSections($secid) as $rec) {
        if ($rec['pic'] ?? false) {
            $items = explode("\r\n",$rec['content']); 
            $firstItem= reset($items);
            unset($items[key($items)]);
            nodes("div",node
                        ("div",["img",null,'src="/img/pages/'.$rec['pic'].'"'],"class='img-$name'")
                        ("div",$firstItem,null)
                        ("ul",node(listElements($items)),null)
                        ,"class='imgpane-$name'");
        } else
            nodes("p",$rec['content'],"class='text-$name'");
    }
}
~~~
