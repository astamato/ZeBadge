package de.berlindroid.zeapp.zeui.zehome

import de.berlindroid.zeapp.zemodels.ZeSlot

 internal val ZeSlot.isSponsor: Boolean
    get() = this is ZeSlot.FirstSponsor

