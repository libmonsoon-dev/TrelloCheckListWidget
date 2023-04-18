package com.github.libmonsoon.trellochecklistwidget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.github.libmonsoon.trelloapi.domain.*

class SettingsActivity : AppCompatActivity() {
    private var apiClient: TrelloClient? = null

    //TODO: support to multiple check-lists in one widget
    private var member: Member? = null
    private var boards: Array<Board>? = null
    private var selectedBoard: Board? = null
    private var lists: Array<CardsList>? = null
    private var selectedList: CardsList? = null
    private var cards: List<Card>? = null
    private var selectedCard: Card? = null
    private var checkLists: Array<CheckList>? = null
    private var selectedCheckList: CheckList? = null

    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initApiClient()
        backgroundLoad()
    }

    private fun initApiClient() {
        //TODO: get args from preferences
        //TODO: https://developer.atlassian.com/cloud/trello/guides/rest-api/authorization/
        apiClient = TrelloClient(getString(R.string.trello_api_key), getString(R.string.trello_token))
    }


    private fun backgroundLoad() {
        if (isLoading) {
            return
        }

        Thread {
            try {
                // TODO: maybe try
                // runOnUiThread({
                isLoading = true
                // })
                load()
            } catch (e: Exception) {
                showError(e.stackTraceToString()) //TODO: error handling
            } finally {
                // TODO: maybe try
                // runOnUiThread({
                isLoading = false
                // })
            }


        }.start()
    }

    private fun load() {
        if (apiClient == null) {
            logV("api client is null")
            return
        }

        loadMember()
        if (member == null) {
            return
        }

        loadBoards()
        if (boards == null) {
            return
        }

        //TODO: select first item if got one-item array
        if (selectedBoard == null) {
            selectedBoard = boards?.find { b: Board -> b.name == "Real life" }
        } //TODO: get from UI
        logV("selected board: ${selectedBoard?.name}")
        if (selectedBoard == null) {
            return
        }

        loadLists()
        if (lists == null) {
            return
        }

        if (selectedList == null) {
            selectedList = lists?.find { list: CardsList -> list.name == "main" }
        } //TODO: get from UI
        logV("selected list: ${selectedList?.name}")
        if (selectedList == null) {
            return
        }

        loadCards()
        if (cards == null) {
            return
        }

        if (selectedCard == null) {
            selectedCard = cards?.find { c: Card -> c.name == "Бкк" } //TODO: get from UI
        }
        logV("selected card: ${selectedCard?.name}")
        if (selectedCard == null) {
            return
        }

        loadCheckLists()
        if (checkLists == null) {
            return
        }

        if (selectedCheckList == null) {
            selectedCheckList = checkLists?.find { list: CheckList -> list.name == "Checklist" }
        } //TODO: get from UI
        logV("${selectedCard?.name} checklist items:\n${
            selectedCheckList?.checkItems?.joinToString("\n") { item: CheckItem ->
                "${if (item.state == "complete") "\uD83D\uDDF9" else "☐"} ${item.name}"
            }
        }")
    }

    private fun loadMember() {
        // TODO: maybe try
        // runOnUiThread({

        if (member == null) {
            member = apiClient?.loadMember()
        }
        logV("user: ${member?.email}")
    }

    private fun loadBoards() {
        // TODO: maybe try
        // runOnUiThread({
        if (boards == null) {
            boards = apiClient?.loadMemberBoards(member?.id!!)
        }
        logV("boards: ${boards?.map { b: Board -> b.name }}")
    }

    private fun loadLists() {
        // TODO: maybe try
        // runOnUiThread({
        if (lists == null) {
            lists = apiClient?.loadBoardLists(selectedBoard?.id!!)
        }
        logV("lists: ${lists?.map { list: CardsList -> list.name }}")
    }

    private fun loadCards() {
        // TODO: maybe try
        // runOnUiThread({
        if (cards == null) {
            cards = apiClient
                ?.loadBoardCards(selectedBoard?.id!!)
                ?.filter { card: Card -> card.listId == selectedList?.id }
        }
        logV("cards: ${cards?.map { c: Card -> c.name }}")
    }

    private fun loadCheckLists() {
        // TODO: maybe try
        // runOnUiThread({
        if (checkLists == null) {
            checkLists = apiClient?.loadCardCheckLists(selectedCard?.id!!)
        }
        logV("check lists: ${checkLists?.map { list: CheckList -> list.name }}")
    }

    private fun refresh() {
        clear()
        backgroundLoad()
    }

    private fun clear() {
        boards = null
        selectedBoard = null
        lists = null
        selectedList = null
        cards = null
        selectedCard = null
        checkLists = null
        selectedCheckList = null
    }

    private fun showError(err: String) {
        val intent = Intent(this, ErrorActivity::class.java)
        intent.putExtra(ErrorActivity.EXTRA_KEY, err)

// TODO: migrate to new API
//        ActivityResultContract
//        ActivityResultContracts.StartActivityForResult
//        this.registerForActivityResult(contract, callback)
//        Use registerForActivityResult(ActivityResultContract, ActivityResultCallback) passing in a StartActivityForResult object for the ActivityResultContract.
        this.startActivityForResult(intent, 0)
    }

    private fun logV(msg: String) = Log.v("SettingsActivity", msg)

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}