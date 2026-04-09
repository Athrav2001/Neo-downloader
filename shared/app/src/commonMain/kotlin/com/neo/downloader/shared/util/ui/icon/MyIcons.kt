package com.neo.downloader.shared.util.ui.icon

import com.neo.downloader.resources.icons.NDMIcons
import com.neo.downloader.resources.icons.*
import com.neo.downloader.shared.util.ui.BaseMyColors
import ir.amirab.util.compose.IconSource

object MyIcons : BaseMyColors() {
    override val appIcon = NDMIcons.AppIcon.asIconSource("appIcon", false)

    override val settings = NDMIcons.Settings.asIconSource("settings")
    override val flag = NDMIcons.Flag.asIconSource("flag")
    override val fast = NDMIcons.Fast.asIconSource("fast")
    override val search = NDMIcons.Search.asIconSource("search")
    override val info = NDMIcons.Info.asIconSource("info")
    override val check = NDMIcons.Check.asIconSource("check")
    override val link = NDMIcons.AddLink.asIconSource("link")
    override val download = NDMIcons.DownSpeed.asIconSource("download")
    override val permission = NDMIcons.Permission.asIconSource("permission")

    override val windowMinimize = NDMIcons.WindowMinimize.asIconSource("windowMinimize")
    override val windowFloating = NDMIcons.WindowFloating.asIconSource("windowFloating")
    override val windowMaximize = NDMIcons.WindowMaximize.asIconSource("windowMaximize")
    override val windowClose = NDMIcons.WindowClose.asIconSource("windowClose")

    override val exit = NDMIcons.Exit.asIconSource("exit")
    override val edit = NDMIcons.Edit.asIconSource("edit")
    override val undo = NDMIcons.Undo.asIconSource("undo")

    override val openSource = NDMIcons.OpenSource.asIconSource("openSource")
    override val telegram = NDMIcons.Telegram.asIconSource("telegram", false)
    override val speaker = NDMIcons.Speaker.asIconSource("speaker")
    override val group = NDMIcons.Group.asIconSource("group")

    override val browserMozillaFirefox = NDMIcons.BrowserMozillaFirefox.asIconSource("browserMozillaFirefox", false)
    override val browserGoogleChrome = NDMIcons.BrowserGoogleChrome.asIconSource("browserGoogleChrome", false)
    override val browserMicrosoftEdge = NDMIcons.BrowserMicrosoftEdge.asIconSource("browserMicrosoftEdge", false)
    override val browserOpera = NDMIcons.BrowserOpera.asIconSource("browserOpera", false)

    override val next = NDMIcons.Next.asIconSource("next")
    override val back = NDMIcons.Back.asIconSource("back")
    override val up = NDMIcons.Up.asIconSource("up")
    override val down = NDMIcons.Down.asIconSource("down")

    override val activeCount = NDMIcons.List.asIconSource("activeCount")
    override val speed = NDMIcons.DownSpeed.asIconSource("speed")

    override val resume = NDMIcons.Resume.asIconSource("resume")
    override val pause = NDMIcons.Pause.asIconSource("pause")
    override val stop = NDMIcons.Stop.asIconSource("stop")

    override val queue = NDMIcons.Queue.asIconSource("queue")
    override val queueStart = NDMIcons.QueueStart.asIconSource("queueStart")
    override val queueStop = NDMIcons.QueueStop.asIconSource("queueStop")

    override val remove = NDMIcons.Delete.asIconSource("remove")
    override val clear = NDMIcons.Clear.asIconSource("clear")
    override val add = NDMIcons.Plus.asIconSource("add")
    override val minus = NDMIcons.Minus.asIconSource("add")
    override val paste = NDMIcons.Clipboard.asIconSource("paste")

    override val copy = NDMIcons.Copy.asIconSource("copy")
    override val refresh = NDMIcons.Refresh.asIconSource("refresh")
    override val editFolder = NDMIcons.Folder.asIconSource("editFolder")

    override val share = NDMIcons.Share.asIconSource("share")
    override val file = NDMIcons.File.asIconSource("file")
    override val folder = NDMIcons.Folder.asIconSource("folder")

    override val fileOpen = file
    override val folderOpen = folder
    override val pictureFile = NDMIcons.FilePicture.asIconSource("fileOpen")
    override val musicFile = NDMIcons.FileMusic.asIconSource("folderOpen")
    override val zipFile = NDMIcons.FileZip.asIconSource("pictureFile")
    override val videoFile = NDMIcons.FileVideo.asIconSource("musicFile")
    override val applicationFile = NDMIcons.FileApplication.asIconSource("zipFile")
    override val documentFile = NDMIcons.FileDocument.asIconSource("videoFile")
    override val otherFile = NDMIcons.FileUnknown.asIconSource("applicationFile")

    override val lock = NDMIcons.Lock.asIconSource("lock")
    override val question = NDMIcons.QuestionMark.asIconSource("question")

    override val grip = NDMIcons.Grip.asIconSource("grip")
    override val sortUp = NDMIcons.Sort123.asIconSource("sortUp")
    override val sortDown = NDMIcons.Sort321.asIconSource("sortDown")
    override val verticalDirection = NDMIcons.VerticalDirection.asIconSource("verticalDirection")

    override val browserIntegration = NDMIcons.Earth.asIconSource("browserIntegration")
    override val appearance = NDMIcons.Colors.asIconSource("appearance")
    override val downloadEngine = NDMIcons.DownSpeed.asIconSource("downloadEngine")
    override val network = NDMIcons.Network.asIconSource("network")
    override val language = NDMIcons.Language.asIconSource("language")

    override val externalLink = NDMIcons.ExternalLink.asIconSource("externalLink")
    override val earth = NDMIcons.Earth.asIconSource("earth")
    override val hearth = NDMIcons.Hearth.asIconSource("hearth")
    override val dragAndDrop = NDMIcons.DragAndDrop.asIconSource("dragAndDrop")


    override val selectAll = NDMIcons.SelectAll.asIconSource("selectAll")
    override val selectInside = NDMIcons.SelectInside.asIconSource("selectInside")
    override val selectInvert = NDMIcons.SelectInvert.asIconSource("selectInvert")

    override val menu = NDMIcons.Menu.asIconSource("menu")

    override val close: IconSource = NDMIcons.Clear.asIconSource("close")

    override val data: IconSource = NDMIcons.Data.asIconSource("alphabet")
    override val alphabet: IconSource = NDMIcons.Alphabet.asIconSource("alphabet")
    override val clock: IconSource = NDMIcons.Clock.asIconSource("clock")
}
