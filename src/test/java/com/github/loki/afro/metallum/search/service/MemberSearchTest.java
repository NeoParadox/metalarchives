package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.LinkCategory;
import com.github.loki.afro.metallum.search.query.MemberSearchQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class MemberSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noMemberNameTest() {
        final MemberSearchService searchService = new MemberSearchService();
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("");
        try {
            searchService.performSearch(query);
            Assert.fail();
        } catch (MetallumException e) {
            Assert.assertFalse(e.getMessage().isEmpty());
        }
    }

    @Test
    public void searchNameTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService();
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Fenriz");
        final List<Member> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        defaultSearchMemberCheck(result);
    }

    @Test
    public void photoTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, true, false, false);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Eicca Toppinen");
        final List<Member> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        Assert.assertFalse(member.getPhotoUrl().isEmpty());
        Assert.assertNotNull(member.getPhoto());
    }

    @Test
    public void detailsTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, false, false, true);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("George \"Corpsegrinder\" Fisher");
        final List<Member> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        Assert.assertFalse(member.getPhotoUrl().isEmpty());
        Assert.assertTrue(member.getDetails().startsWith("George"));
        Assert.assertTrue(member.getDetails().endsWith("Forgotten."));
    }

    @Test
    public void linksTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, false, true, false);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Tom G. Warrior");
        final List<Member> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        Assert.assertFalse(member.getPhotoUrl().isEmpty());
        defaulLinkTest(member.getLinks());
    }

    private void defaulLinkTest(final Collection<Link> links) {
        for (final Link link : links) {
            Assert.assertFalse(link.getName().isEmpty());
            Assert.assertFalse(link.getURL().isEmpty());
            Assert.assertNotSame(link.getCategory(), LinkCategory.ANY);
        }
    }

    private void defaultSearchMemberCheck(final Member member) {
        Assert.assertNotSame(0L, member.getId());
        Assert.assertFalse(member.getName().isEmpty());
        Assert.assertNotNull(member.getRealName());
        Assert.assertNotSame(Country.ANY, member.getCountry());
    }

    private void defaultSearchMemberCheck(final Collection<Member> membersToTest) {
        for (final Member member : membersToTest) {
            defaultSearchMemberCheck(member);
        }
    }
}
