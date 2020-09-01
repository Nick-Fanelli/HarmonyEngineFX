const projectXML = `<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<Project>
    <value name="ProjectName" value="Game"/>
    <value name="HarmonyVersion" value="version-1.0.0"/>
    <value name="Author" value="Company Name"/>
    <value name="VersionID" value="1.0.0"/>
    <value name="LauncherState" value="Main State"/>
    <Textures>
        <texture id="0" name="Facebook Logo" path="/Resources/Textures/facebook.png"/>
        <texture id="1" name="Twitter Logo" path="/Resources/Textures/twitter.png"/>
        <texture id="2" name="Tileset" path="/Resources/Textures/Tileset.png"/>
    </Textures>
    <GameObjects>
        <gameObject>
            <data name="Twitter"/>
            <data textureID="1"/>
            <vector2f x="0.0" y="0.0"/>
        </gameObject>
        <gameObject>
            <data name="Twitter Brid!!"/>
            <data textureID="1"/>
            <vector2f x="0.0" y="0.0"/>
        </gameObject>
        <gameObject>
            <data name="Ground Tileset"/>
            <data textureID="2"/>
            <vector2f x="0.0" y="0.0"/>
        </gameObject>
    </GameObjects>
    <States>
        <state>
            <data name="Main State"/>
            <Hierarchy>
                <gameObject>
                    <data name="Twitter Brid!!"/>
                    <data textureID="1"/>
                    <vector2f x="-8.583099" y="-19.728699"/>
                </gameObject>
                <gameObject>
                    <data name="Twitter Brid!!"/>
                    <data textureID="2"/>
                    <vector2f x="10.583099" y="10.728699"/>
                </gameObject>
            </Hierarchy>
        </state>
    </States>
</Project>
`